// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Yrpc.proto

package io.github.s3s3l.yggdrasil.rpc.grpc.yrpc;

public final class YrpcProto {
  private YrpcProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_yrpc_Request_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_yrpc_Request_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_yrpc_Response_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_yrpc_Response_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\nYrpc.proto\022\004yrpc\"Q\n\007Request\022\025\n\rseriali" +
      "zeType\030\001 \001(\t\022\017\n\007service\030\002 \001(\t\022\020\n\010argType" +
      "s\030\003 \003(\t\022\014\n\004args\030\004 \003(\014\"3\n\010Response\022\014\n\004cod" +
      "e\030\001 \001(\t\022\013\n\003msg\030\002 \001(\t\022\014\n\004data\030\003 \001(\01422\n\004Yr" +
      "pc\022*\n\007request\022\r.yrpc.Request\032\016.yrpc.Resp" +
      "onse\"\000B<\n\'io.github.s3s3l.yggdrasil.rpc." +
      "grpc.yrpcB\tYrpcProtoP\001\242\002\003RTGb\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_yrpc_Request_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_yrpc_Request_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_yrpc_Request_descriptor,
        new java.lang.String[] { "SerializeType", "Service", "ArgTypes", "Args", });
    internal_static_yrpc_Response_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_yrpc_Response_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_yrpc_Response_descriptor,
        new java.lang.String[] { "Code", "Msg", "Data", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
