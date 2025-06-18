package com.tagzxia.tagzxia1.propguardian.src.main.java.com.talexframe.frame.config;

import com.talexframe.frame.core.talex.TFrame;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 * SSL (HTTP & HTTPS) <br /> {@link com.talexframe.frame.config Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/24 16:48 <br /> Project: TalexFrame <br />
 */
@Configuration
@Slf4j
public class ConnectorConfig {

    private static boolean HTTPS = false;

    static {

        if ( !new File(TFrame.getMainFile() + "/config/keystore.p12").exists() ) {

            log.info("[Connector] 未检测到 keystore -> 启动 HTTP 服务");

        } else {

            HTTPS = true;

            log.info("[Connector] 已检测到 keystore -> 启动 HTTPS 服务");

        }

    }

    @Bean
    public TomcatServletWebServerFactory servletContainer() { //springboot2 新变化

        if ( HTTPS ) {

            TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {

                @Override
                protected void postProcessContext(Context context) {

                    SecurityConstraint securityConstraint = new SecurityConstraint();
                    securityConstraint.setUserConstraint("CONFIDENTIAL");
                    SecurityCollection collection = new SecurityCollection();
                    collection.addPattern("/*");
                    securityConstraint.addCollection(collection);
                    context.addConstraint(securityConstraint);
                }
            };
            tomcat.addAdditionalTomcatConnectors(initiateHttpConnector());
            return tomcat;

        } else {

            return new TomcatServletWebServerFactory();

        }


    }

    private Connector initiateHttpConnector() {

        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(8080);//http端口
        connector.setSecure(true);//设置为false重定向容易出错，建议设置为true
        connector.setRedirectPort(443);
        return connector;
    }

}
