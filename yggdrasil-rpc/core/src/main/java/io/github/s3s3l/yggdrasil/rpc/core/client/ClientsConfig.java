package io.github.s3s3l.yggdrasil.rpc.core.client;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ClientsConfig {
    private String register;
    private List<ClientConfig> clients;
}
