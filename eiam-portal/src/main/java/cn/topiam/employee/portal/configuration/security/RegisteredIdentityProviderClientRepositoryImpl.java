/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.portal.configuration.security;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.topiam.employee.authentication.common.client.IdentityProviderConfig;
import cn.topiam.employee.authentication.common.client.RegisteredIdentityProviderClient;
import cn.topiam.employee.authentication.common.client.RegisteredIdentityProviderClientRepository;
import cn.topiam.employee.common.entity.authn.IdentityProviderEntity;
import cn.topiam.employee.common.repository.authentication.IdentityProviderRepository;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2024/3/24 20:39
 */
@Component
public class RegisteredIdentityProviderClientRepositoryImpl implements
                                                            RegisteredIdentityProviderClientRepository {

    /**
     * 根据code查询身份提供商配置
     *
     * @param code {@link String}
     * @return {@link RegisteredIdentityProviderClient}
     */
    @Override
    public <T extends IdentityProviderConfig> Optional<RegisteredIdentityProviderClient<T>> findByCode(String code) {
        //@formatter:off
        Optional<IdentityProviderEntity> optional = identityProviderRepository.findByCodeAndEnabledIsTrue(code);
        if (optional.isPresent()) {
            try {
                // 指定序列化输入的类型
                ObjectMapper objectMapper=new ObjectMapper();
                objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
                IdentityProviderEntity entity = optional.get();
                T provider = objectMapper.readValue(entity.getConfig(), new TypeReference<>() {});
                RegisteredIdentityProviderClient<T> client = RegisteredIdentityProviderClient.<T>builder()
                        .id(String.valueOf(entity.getId()))
                        .code(entity.getCode())
                        .name(entity.getName())
                        .config(provider)
                        .build();
                return Optional.of(client);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        //@formatter:on
        return Optional.empty();
    }

    private final IdentityProviderRepository identityProviderRepository;

    public RegisteredIdentityProviderClientRepositoryImpl(IdentityProviderRepository identityProviderRepository) {
        this.identityProviderRepository = identityProviderRepository;
    }

}
