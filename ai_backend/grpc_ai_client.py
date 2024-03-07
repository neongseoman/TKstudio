import grpc
import ddalkkak_pb2_grpc as pb2_grpc
import ddalkkak_pb2 as pb2


class DdalkkakClient(object):
    def __init__(self):
        self.host = "localhost"
        self.server_port = 50051

        self.channel = grpc.insecure_channel(
            "{}:{}".format(self.host, self.server_port)
        )

        self.stub = pb2_grpc.DdalkkakStub(self.channel)

    def get_url(self, name):
        name = pb2.TestRequest(name=name)
        print(f"{name}")
        return self.stub.TestFunc(name)


if __name__ == "__main__":
    client = DdalkkakClient()
    result = client.get_url(name="박근수")
    print(f"{result}")
