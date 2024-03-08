import grpc

import image_pb2 as pb2
import image_pb2_grpc as pb2_grpc

import matplotlib.pyplot as plt


class TestClient(object):
    def __init__(self):
        self.host = "localhost"
        self.server_port = 50051

        self.channel = grpc.insecure_channel(
            "{}:{}".format(self.host, self.server_port)
        )

        self.stub = pb2_grpc.CreateImageStub(self.channel)

    def inputImage(self, originalImage, options):
        OriginalImageInfo = pb2.OriginalImageInfo(
            originalImage=originalImage, options=options
        )

        return self.stub.sendImage(OriginalImageInfo)


if __name__ == "__main__":
    client = TestClient()
    original_image_path = "./rdj.jpg"
    original_image = plt.imread(original_image_path)
    original_image = original_image.tobytes()
    options = {
        "background": "0",
        "suit": "0",
        "hair": "0",
    }

    result = client.inputImage(originalImage=original_image, options=options)
    # print(f"result:\n{result}")
    print("processedImage:")
    print(result.processedImage)
    print()
    print("responseUrl:")
    print(result.responseUrl)
