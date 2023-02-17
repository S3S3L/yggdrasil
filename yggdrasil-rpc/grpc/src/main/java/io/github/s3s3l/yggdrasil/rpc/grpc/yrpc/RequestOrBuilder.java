// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Yrpc.proto

package io.github.s3s3l.yggdrasil.rpc.grpc.yrpc;

public interface RequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:yrpc.Request)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string serializeType = 1;</code>
   * @return The serializeType.
   */
  java.lang.String getSerializeType();
  /**
   * <code>string serializeType = 1;</code>
   * @return The bytes for serializeType.
   */
  com.google.protobuf.ByteString
      getSerializeTypeBytes();

  /**
   * <code>string service = 2;</code>
   * @return The service.
   */
  java.lang.String getService();
  /**
   * <code>string service = 2;</code>
   * @return The bytes for service.
   */
  com.google.protobuf.ByteString
      getServiceBytes();

  /**
   * <code>repeated string argTypes = 3;</code>
   * @return A list containing the argTypes.
   */
  java.util.List<java.lang.String>
      getArgTypesList();
  /**
   * <code>repeated string argTypes = 3;</code>
   * @return The count of argTypes.
   */
  int getArgTypesCount();
  /**
   * <code>repeated string argTypes = 3;</code>
   * @param index The index of the element to return.
   * @return The argTypes at the given index.
   */
  java.lang.String getArgTypes(int index);
  /**
   * <code>repeated string argTypes = 3;</code>
   * @param index The index of the value to return.
   * @return The bytes of the argTypes at the given index.
   */
  com.google.protobuf.ByteString
      getArgTypesBytes(int index);

  /**
   * <code>repeated bytes args = 4;</code>
   * @return A list containing the args.
   */
  java.util.List<com.google.protobuf.ByteString> getArgsList();
  /**
   * <code>repeated bytes args = 4;</code>
   * @return The count of args.
   */
  int getArgsCount();
  /**
   * <code>repeated bytes args = 4;</code>
   * @param index The index of the element to return.
   * @return The args at the given index.
   */
  com.google.protobuf.ByteString getArgs(int index);
}
