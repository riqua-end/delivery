package com.delivery.apigw.route

import com.delivery.apigw.filter.ServiceApiPrivateFilter
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RouteConfig(
    private val serviceApiPrivateFilter: ServiceApiPrivateFilter
) {
    @Bean
    fun gatewayRoutes(builder: RouteLocatorBuilder): RouteLocator {

        return builder.routes()
            .route {spec ->
                spec.order(-1) // 우선순위
                spec.path("/service-api/api/**") // 매칭 주소
                    .filters { filterSpec ->
                        filterSpec.filter(serviceApiPrivateFilter.apply(ServiceApiPrivateFilter.Config()))
                        filterSpec.rewritePath("/service-api(?<segment>/?.*)", "\${segment}")
                    }.uri(
                        "http://localhost:8181" // 라우팅 주소
                    )
            }
            .build()
    }
}