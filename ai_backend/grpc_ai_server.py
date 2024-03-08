import grpc
from concurrent import futures
import time

import image_pb2 as pb2
import image_pb2_grpc as pb2_grpc


class CreateImageService(pb2_grpc.CreateImageServicer):

    def __init__(self, *args, **kwargs):
        pass

    def sendImage(self, request, context):

        original_image = request.originalImage
        # options = request.options

        # 이미지 가공하는 코드
        processed_image = original_image

        # S3에 이미지 저장하는 코드

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
    print("grpc server is now running")
    print("if you want to quit the grpc server, press 'ctrl + C'")
    server.start()
    server.wait_for_termination()


if __name__ == "__main__":
    serve()
