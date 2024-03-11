import grpc
from concurrent import futures

import image_pb2 as pb2
import image_pb2_grpc as pb2_grpc
import numpy as np
import cv2

# insightface
import insightface
from insightface.app import FaceAnalysis
import matplotlib.pyplot as plt

# face swap app
faceswap_app = FaceAnalysis(name="buffalo_l")
faceswap_app.prepare(ctx_id=0, det_size=(640, 640))

# swapper model
swapper_path = "./inswapper_128.onnx"
swapper = insightface.model_zoo.get_model(
    swapper_path, download=False, download_zip=False
)

# img dir
bg_0_img_path = "./bg_0.jpg"
bg_0_img = plt.imread(bg_0_img_path)


class CreateImageService(pb2_grpc.CreateImageServicer):

    def __init__(self, *args, **kwargs):
        pass

    def sendImage(self, request, context):

        original_image_bytes: bytearray = request.originalImage
        # bytes to ndarray
        image_array = np.frombuffer(original_image_bytes, dtype=np.uint8)
        # options = request.options

        original_image = cv2.imdecode(image_array, cv2.IMREAD_COLOR)
        # print("오리지널 이미지 사이즈", original_image.shape)
        # cv2.imshow("Image", original_image)
        # cv2.waitKey(0)

        ############################################################
        # # detect face from bg img
        bg_faces = faceswap_app.get(bg_0_img)
        bg_face = bg_faces[0]

        # detect face from input img
        faces = faceswap_app.get(original_image)
        source_face = faces[0]

        # swap face from bg img to input img
        processed_image = bg_0_img.copy()
        processed_image = swapper.get(bg_0_img, bg_face, source_face, paste_back=True)
        ############################################################

        # 이미지 확인용
        # plt.imshow(processed_image)
        # plt.show()

        # ndarray to bytes
        processed_image = processed_image.tobytes()

        # S3에 이미지 저장하는 코드 추가하기

        response_url = {
            "originalImageUrl": "original_image_url_sample",
            "processedImageUrl": "processed_image_url_sample",
            "thumbnailImageUrl": "thumbnail_image_url_sample",
        }

        result = {
            "processedImage": processed_image,
            "responseUrl": response_url,
        }

        return pb2.ProcessedImageInfo(**result)


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=2))
    pb2_grpc.add_CreateImageServicer_to_server(CreateImageService(), server)
    server.add_insecure_port("[::]:50051")
    print("server port is", 50051)
    server.start()
    print("grpc server is now running")
    print("if you want to quit the grpc server, press 'ctrl + C'")
    print()
    print()
    print()
    print()
    server.wait_for_termination()


if __name__ == "__main__":
    serve()
