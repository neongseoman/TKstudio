import grpc
from concurrent import futures

from proto import image_pb2 as pb2
from proto import image_pb2_grpc as pb2_grpc
import numpy as np
import cv2

# Insightface
import insightface
from insightface.app import FaceAnalysis
import matplotlib.pyplot as plt

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

    def sendImage(self, request, context):
        try:
            original_image_bytes: bytearray = request.originalImage

            # Bytes to ndarray
            image_array = np.frombuffer(original_image_bytes, dtype=np.uint8)
            original_image = cv2.imdecode(image_array, cv2.IMREAD_COLOR)
            # options = request.options  # 이후 옵션에 따라 템플릿사진 선택
            # gender = options.gender
            # bg = options.background
            # suit = options.suit
            # hair = options.hair

            # Detect face from bg img

            # Img dir
            gender = "male"  # temp gender
            bg_0_img_path = f"./src/{gender}_bg_0.jpg"  # f-string 사용해서 옵션에 따라 bg image 변경
            bg_0_img = plt.imread(bg_0_img_path)
            bg_faces = faceswap_app.get(bg_0_img)
            bg_face = bg_faces[0]

            try:
                # Detect face from input img
                faces = faceswap_app.get(original_image)
                source_face = faces[0]

            except Exception as err:
                # Handle error: No face detected or other issues
                print(f"Error detecting face in original image: {err}")

                # context.set_code(grpc.StatusCode.UNAVAILABLE)
                # context.set_details("Failed to detect face in input image.")

                # response_url = {
                #     "originalImageUrl": "",
                #     "processedImageUrl": "",
                #     "thumbnailImageUrl": "",
                # }

                # result = {
                #     "processedImage": b"0",
                #     "responseUrl": response_url,
                #     "status": False,
                #     "errorMessage": err,
                # }
                # return pb2.ProcessedImageInfo(**result)
                return None

            # Swap face from bg img to input img
            processed_image = bg_0_img.copy()
            processed_image = swapper.get(
                bg_0_img, bg_face, source_face, paste_back=True
            )

            # 이미지 확인용
            plt.imshow(processed_image)
            plt.show()
            processed_image = cv2.cvtColor(
                processed_image, cv2.COLOR_BGR2RGB
            )  # BGR -> RGB 채널 변경

            # 사진 저장
            cv2.imwrite("src/output_image.jpg", processed_image)

            # Ndarray to bytes
            processed_image = processed_image.tobytes()

            # Add codes to save images in S3 server

            response_url = {
                "originalImageUrl": "original_image_url_sample",
                "processedImageUrl": "processed_image_url_sample",
                "thumbnailImageUrl": "thumbnail_image_url_sample",
            }

            result = {
                "processedImage": processed_image,
                "responseUrl": response_url,
                # "status": True,
                # "errorMessage": None,
            }

            return pb2.ProcessedImageInfo(**result)

        except Exception as err:
            # Handle error: Failed to process image
            print(f"Error processing image: {err}")

            # response_url = {
            #     "originalImageUrl": "",
            #     "processedImageUrl": "",
            #     "thumbnailImageUrl": "",
            # }

            # result = {
            #     "processedImage": b"0",
            #     "responseUrl": response_url,
            #     "status": False,
            #     "errorMessage": err,
            # }
            # return pb2.ProcessedImageInfo(**result)
            return None


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=2))
    pb2_grpc.add_CreateImageServicer_to_server(CreateImageService(), server)
    server.add_insecure_port("[::]:9090")
    print("\n" * 1)
    print("server port is", 9090)
    server.start()
    print("grpc server is now running")
    print("if you want to quit the grpc server, press 'ctrl + C'")
    print("\n" * 2)

    server.wait_for_termination()


if __name__ == "__main__":
    serve()
