# Generated by the gRPC Python protocol compiler plugin. DO NOT EDIT!
"""Client and server classes corresponding to protobuf-defined services."""
import grpc

from proto import image_pb2 as image__pb2


class CreateImageStub(object):
    """In dir 'src/proto'
    python -m grpc_tools.protoc --python_out=. --grpc_python_out=. --proto_path=. ./image.proto

    """

    def __init__(self, channel):
        """Constructor.

        Args:
            channel: A grpc.Channel.
        """
        self.sendImage = channel.unary_unary(
            "/com.ssafy.pjt.grpc.CreateImage/sendImage",
            request_serializer=image__pb2.OriginalImageInfo.SerializeToString,
            response_deserializer=image__pb2.ProcessedImageInfo.FromString,
        )


class CreateImageServicer(object):
    """In dir 'src/proto'
    python -m grpc_tools.protoc --python_out=. --grpc_python_out=. --proto_path=. ./image.proto

    """

    def sendImage(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details("Method not implemented!")
        raise NotImplementedError("Method not implemented!")


def add_CreateImageServicer_to_server(servicer, server):
    rpc_method_handlers = {
        "sendImage": grpc.unary_unary_rpc_method_handler(
            servicer.sendImage,
            request_deserializer=image__pb2.OriginalImageInfo.FromString,
            response_serializer=image__pb2.ProcessedImageInfo.SerializeToString,
        ),
    }
    generic_handler = grpc.method_handlers_generic_handler(
        "com.ssafy.pjt.grpc.CreateImage", rpc_method_handlers
    )
    server.add_generic_rpc_handlers((generic_handler,))


# This class is part of an EXPERIMENTAL API.
class CreateImage(object):
    """In dir 'src/proto'
    python -m grpc_tools.protoc --python_out=. --grpc_python_out=. --proto_path=. ./image.proto

    """

    @staticmethod
    def sendImage(
        request,
        target,
        options=(),
        channel_credentials=None,
        call_credentials=None,
        insecure=False,
        compression=None,
        wait_for_ready=None,
        timeout=None,
        metadata=None,
    ):
        return grpc.experimental.unary_unary(
            request,
            target,
            "/com.ssafy.pjt.grpc.CreateImage/sendImage",
            image__pb2.OriginalImageInfo.SerializeToString,
            image__pb2.ProcessedImageInfo.FromString,
            options,
            channel_credentials,
            insecure,
            call_credentials,
            compression,
            wait_for_ready,
            timeout,
            metadata,
        )
