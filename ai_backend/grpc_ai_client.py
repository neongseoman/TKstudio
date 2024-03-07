import grpc
import ddalkkak_pb2 as pb2
import ddalkkak_pb2_grpc as pb2_grpc


class DdalkkakClient(object):
    def __init__(self):
        self.host = "localhost"
        self.server_port = 50051

        self.channel = grpc.insecure_channel(
            "{}:{}".format(self.host, self.server_port)
        )

        self.stub = pb2_grpc.DdalkkakStub(self.channel)

    def inputImage(self, userImage, options):
        ddalkkakrequest = pb2.DdalkkakRequest(userImage=userImage, options=options)

        return self.stub.ProcessImage(ddalkkakrequest)


if __name__ == "__main__":
    client = DdalkkakClient()
    test_byte = b"Hello, world!"
    result = client.inputImage(userImage=test_byte, options="myOptions")
    print(f"{result}")
