import grpc
from concurrent import futures
import time
import ddalkkak_pb2 as pb2
import ddalkkak_pb2_grpc as pb2_grpc


class DdalkkakService(pb2_grpc.DdalkkakServicer):

    def __init__(self, *args, **kwargs):
        pass

    def ProcessImage(self, request, context):

        userImage = request.userImage
        options = request.options

        # 이미지 가공하는 코드

        result = {
            "originalS3URL": "originalS3URL",
            "processed3SURL": "processed3SURL",
            "thumbnailS3URL": "thumbnailS3URL",
            "options": options,
            "processedImage": userImage,
        }

        return pb2.DdalkkakReply(**result)


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=2))
    pb2_grpc.add_DdalkkakServicer_to_server(DdalkkakService(), server)
    server.add_insecure_port("[::]:50051")
    print("server port is", 50051)
    print("grpc server is now running")
    server.start()
    server.wait_for_termination()


if __name__ == "__main__":
    serve()
