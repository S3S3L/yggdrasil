package io.github.s3s3l.yggdrasil.rpc.grpc.yrpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.51.0)",
    comments = "Source: Yrpc.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class YrpcGrpc {

  private YrpcGrpc() {}

  public static final String SERVICE_NAME = "yrpc.Yrpc";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<io.github.s3s3l.yggdrasil.rpc.grpc.yrpc.Request,
      io.github.s3s3l.yggdrasil.rpc.grpc.yrpc.Response> getRequestMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "request",
      requestType = io.github.s3s3l.yggdrasil.rpc.grpc.yrpc.Request.class,
      responseType = io.github.s3s3l.yggdrasil.rpc.grpc.yrpc.Response.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<io.github.s3s3l.yggdrasil.rpc.grpc.yrpc.Request,
      io.github.s3s3l.yggdrasil.rpc.grpc.yrpc.Response> getRequestMethod() {
    io.grpc.MethodDescriptor<io.github.s3s3l.yggdrasil.rpc.grpc.yrpc.Request, io.github.s3s3l.yggdrasil.rpc.grpc.yrpc.Response> getRequestMethod;
    if ((getRequestMethod = YrpcGrpc.getRequestMethod) == null) {
      synchronized (YrpcGrpc.class) {
        if ((getRequestMethod = YrpcGrpc.getRequestMethod) == null) {
          YrpcGrpc.getRequestMethod = getRequestMethod =
              io.grpc.MethodDescriptor.<io.github.s3s3l.yggdrasil.rpc.grpc.yrpc.Request, io.github.s3s3l.yggdrasil.rpc.grpc.yrpc.Response>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "request"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.github.s3s3l.yggdrasil.rpc.grpc.yrpc.Request.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.github.s3s3l.yggdrasil.rpc.grpc.yrpc.Response.getDefaultInstance()))
              .setSchemaDescriptor(new YrpcMethodDescriptorSupplier("request"))
              .build();
        }
      }
    }
    return getRequestMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static YrpcStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<YrpcStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<YrpcStub>() {
        @java.lang.Override
        public YrpcStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new YrpcStub(channel, callOptions);
        }
      };
    return YrpcStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static YrpcBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<YrpcBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<YrpcBlockingStub>() {
        @java.lang.Override
        public YrpcBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new YrpcBlockingStub(channel, callOptions);
        }
      };
    return YrpcBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static YrpcFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<YrpcFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<YrpcFutureStub>() {
        @java.lang.Override
        public YrpcFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new YrpcFutureStub(channel, callOptions);
        }
      };
    return YrpcFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class YrpcImplBase implements io.grpc.BindableService {

    /**
     */
    public void request(io.github.s3s3l.yggdrasil.rpc.grpc.yrpc.Request request,
        io.grpc.stub.StreamObserver<io.github.s3s3l.yggdrasil.rpc.grpc.yrpc.Response> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRequestMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getRequestMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                io.github.s3s3l.yggdrasil.rpc.grpc.yrpc.Request,
                io.github.s3s3l.yggdrasil.rpc.grpc.yrpc.Response>(
                  this, METHODID_REQUEST)))
          .build();
    }
  }

  /**
   */
  public static final class YrpcStub extends io.grpc.stub.AbstractAsyncStub<YrpcStub> {
    private YrpcStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected YrpcStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new YrpcStub(channel, callOptions);
    }

    /**
     */
    public void request(io.github.s3s3l.yggdrasil.rpc.grpc.yrpc.Request request,
        io.grpc.stub.StreamObserver<io.github.s3s3l.yggdrasil.rpc.grpc.yrpc.Response> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRequestMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class YrpcBlockingStub extends io.grpc.stub.AbstractBlockingStub<YrpcBlockingStub> {
    private YrpcBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected YrpcBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new YrpcBlockingStub(channel, callOptions);
    }

    /**
     */
    public io.github.s3s3l.yggdrasil.rpc.grpc.yrpc.Response request(io.github.s3s3l.yggdrasil.rpc.grpc.yrpc.Request request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRequestMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class YrpcFutureStub extends io.grpc.stub.AbstractFutureStub<YrpcFutureStub> {
    private YrpcFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected YrpcFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new YrpcFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<io.github.s3s3l.yggdrasil.rpc.grpc.yrpc.Response> request(
        io.github.s3s3l.yggdrasil.rpc.grpc.yrpc.Request request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRequestMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_REQUEST = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final YrpcImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(YrpcImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_REQUEST:
          serviceImpl.request((io.github.s3s3l.yggdrasil.rpc.grpc.yrpc.Request) request,
              (io.grpc.stub.StreamObserver<io.github.s3s3l.yggdrasil.rpc.grpc.yrpc.Response>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class YrpcBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    YrpcBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return io.github.s3s3l.yggdrasil.rpc.grpc.yrpc.YrpcProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Yrpc");
    }
  }

  private static final class YrpcFileDescriptorSupplier
      extends YrpcBaseDescriptorSupplier {
    YrpcFileDescriptorSupplier() {}
  }

  private static final class YrpcMethodDescriptorSupplier
      extends YrpcBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    YrpcMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (YrpcGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new YrpcFileDescriptorSupplier())
              .addMethod(getRequestMethod())
              .build();
        }
      }
    }
    return result;
  }
}
