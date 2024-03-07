import grpc
from concurrent import futures
import time
import ddalkkak_pb2 as pb2
import ddalkkak_pb2_grpc as pb2_grpc


class DdalkkakService(pb2_grpc.DdalkkakServicer):

    def __init__(self, *args, **kwargs):
        pass

    def TestFunc(self, request, context):

        name = request.name
        result = f'Hello I am up and running received "{name}" message from you'
        result = {"code": "codestring", "name": name}

        return pb2.TestReply(**result)


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=2))
    pb2_grpc.add_DdalkkakServicer_to_server(DdalkkakService(), server)
    server.add_insecure_port("[::]:50051")
    server.start()
    server.wait_for_termination()


if __name__ == "__main__":
    serve()
