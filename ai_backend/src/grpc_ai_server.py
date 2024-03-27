# AWS S3
import os
from dotenv import load_dotenv
import boto3

# gRPC
import grpc
from concurrent import futures

from proto import image_pb2 as pb2
from proto import image_pb2_grpc as pb2_grpc

# Insightface
import insightface
from insightface.app import FaceAnalysis

# Etc
import matplotlib.pyplot as plt
import numpy as np
import cv2
import uuid

import logging

# AWS S3 init setting
load_dotenv()
PORT_NUM = os.environ.get("PORT_NUM")
BUCKET_NAME = os.environ.get("BUCKET_NAME")
REGION_NAME = os.environ.get("REGION_NAME")
S3_ACCESS_KEY = os.environ.get("S3_ACCESS_KEY")
S3_SECRET_ACCESS_KEY = os.environ.get("S3_SECRET_ACCESS_KEY")

# S3 client
s3 = boto3.resource(
    "s3",
    region_name=REGION_NAME,
    aws_access_key_id=S3_ACCESS_KEY,
    aws_secret_access_key=S3_SECRET_ACCESS_KEY,
)

buckets = s3.Bucket(name=BUCKET_NAME)


# Face swap app
faceswap_app = FaceAnalysis(name="buffalo_l")
faceswap_app.prepare(ctx_id=0, det_size=(640, 640))

# Swapper model
swapper_path = "./inswapper_128.onnx"
swapper = insightface.model_zoo.get_model(
    swapper_path, download=False, download_zip=False
)


class CreateImageService(pb2_grpc.CreateImageServicer):

    def __init__(self, *args, **kwargs):
        pass

    # Upload image in S3 server
    def uploadToS3(self, image_path, type_of_image):
        # image_path: local image path
        # type_of_image: 'original' / 'processed' / 'thumbnail'
        my_uuid = uuid.uuid1()
        name = f"{type_of_image}Images/{my_uuid}.png"
        data = open(image_path, "rb")
        s3.Bucket(BUCKET_NAME).put_object(
            Key=name,
            Body=data,
            ContentType="image/png",
        )

        return_url = (
            f"https://ddalkkak101-bucket.s3.ap-northeast-2.amazonaws.com/{name}"
        )

        return return_url

    def makeReturnValue(
        self, processed_image=bytes(), response_url=dict(), status="SUCCESS"
    ):
        # processed_image: bytes type
        # response_url: dict type
        # status: 'SUCCESS' / 'NO_FACE' / 'MANY_FACE'
        if status == "SUCCESS":
            result = pb2.ImageProcessingResult.SUCCESS
        elif status == "MANY_FACE":
            result = pb2.ImageProcessingResult.MANY_FACE
        else:
            result = pb2.ImageProcessingResult.NO_FACE

        return_value = {
            "processedImage": processed_image,
            "responseUrl": response_url,
            "result": result,
        }

        return return_value

    def sendImage(self, request, context):
        original_image_bytes: bytearray = request.originalImage

        # Bytes to ndarray
        image_array = np.frombuffer(original_image_bytes, dtype=np.uint8)
        original_image = cv2.imdecode(image_array, cv2.IMREAD_COLOR)

        # Input image save
        save_path = "./image/input_image.png"
        cv2.imwrite(save_path, original_image)

        # Codes to save ORIGINAL image in S3 server
        original_image_url = self.uploadToS3(save_path, "original")

        # Select options by request
        options = request.options
        if options.sex == 0:
            sex = "male"
        else:
            sex = "female"

        suit_option_name = options.optionName

        # DETECT FACE FROM TEMPLATE IMAGE

        # Img dir
        template_img_path = f"./image/{sex}/{suit_option_name}.jpg"
        template_img = plt.imread(template_img_path)
        template_faces = faceswap_app.get(template_img)
        template_face = template_faces[0]

        try:
            # Detect face from input img
            faces = faceswap_app.get(original_image)

            # Many faces in input img
            if len(faces) > 1:
                print("There are more than one face in the input image")
                return_value = self.makeReturnValue(status="MANY_FACE")

                return pb2.ProcessedImageInfo(**return_value)

        # Handle error: No face detected or other issues
        except Exception as err:
            print(f"Error detecting face in original image: {err}")
            return_value = self.makeReturnValue(status="NO_FACE")

            return pb2.ProcessedImageInfo(**return_value)

        # Swap face from template img to input img
        source_face = faces[0]
        processed_image = template_img.copy()
        processed_image = swapper.get(
            template_img, template_face, source_face, paste_back=True
        )

        processed_image = cv2.cvtColor(
            processed_image, cv2.COLOR_BGR2RGB
        )  # BGR -> RGB 채널 변경

        # Output image save
        save_path = "./image/output_image.png"
        cv2.imwrite(save_path, processed_image)

        # Ndarray to bytes
        processed_image = processed_image.tobytes()

        # Codes to save image in S3 server
        try:
            processed_image_url = self.uploadToS3(save_path, "processed")
            thumbnail_image_url = self.uploadToS3(save_path, "thumbnail")

        except:
            print("Error uploading Processed Image to AWS S3 server")
            return_value = self.makeReturnValue(status="NO_FACE")

            return pb2.ProcessedImageInfo(**return_value)

        response_url = {
            "originalImageUrl": original_image_url,
            "processedImageUrl": processed_image_url,
            "thumbnailImageUrl": thumbnail_image_url,
        }

        try:
            print("processed_image\n", processed_image[:25])
            print()
            print("response_url\n", response_url)

            return_value = self.makeReturnValue(
                processed_image=processed_image, response_url=response_url
            )
            print("Success to return")

            return pb2.ProcessedImageInfo(**return_value)

        except:
            return_value = self.makeReturnValue(status="NO_FACE")
            print("Failed to return")

            return pb2.ProcessedImageInfo(**return_value)


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=None))
    pb2_grpc.add_CreateImageServicer_to_server(CreateImageService(), server)
    server.add_insecure_port(f"[::]:{PORT_NUM}")
    print("\n" * 1)
    print("server port is", PORT_NUM)
    server.start()
    print("grpc server is now running")
    print()

    # Check bucket names
    print("Existing buckets:")
    for bucket in s3.buckets.all():
        print(bucket.name)
    print()
    print("End of Bucket list")

    print("if you want to quit the grpc server, press 'ctrl + C'")
    print()

    server.wait_for_termination()


if __name__ == "__main__":
    serve()
