package com.example.lab04.handlers;


import com.example.lab04.config.FilesProperties;
import com.example.lab04.config.RouteFunctionConfig;
import com.example.lab04.models.documents.Categoria;
import com.example.lab04.models.documents.Producto;
import com.example.lab04.models.services.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

//@Disabled
@WebFluxTest()
@Import({RouteFunctionConfig.class, ProductoHandler.class})
@ContextConfiguration(classes = {ProductoHandlerYPTest.TestConfiguration.class})
public class ProductoHandlerYPTest {

    public static class TestConfiguration{
        @Bean
        public FilesProperties filesProperties(){
            FilesProperties filesProperties = new FilesProperties();
            filesProperties.setPath("./");
            return filesProperties;
        }
    }

    @Autowired
    private WebTestClient client;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private FilesProperties filesProperties;

    @BeforeEach
    void resetMocksAndStubs() {
        reset(productoService);
        filesProperties.setInPanic(false);
    }

    @Test
    public void sanity() {
        assertThat(client).isNotNull();
        assertThat(filesProperties).isNotNull();
    }


    @Test

    public void post_createProduct_created(){

        // Preparing data

        Categoria categoria = new Categoria();
        categoria.setId("1");
        categoria.setNombre("Categoria1");

        Date dateSystem = new Date();

        Producto productoToCreated =  new Producto();
        productoToCreated.setPrecio(1.5d);
        productoToCreated.setNombre("producto1");
        productoToCreated.setCategoria(categoria);

        Producto productoCreated =  new Producto();
        productoCreated.setId("abc123");
        productoCreated.setPrecio(1.5d);
        productoCreated.setNombre("producto1");
        productoCreated.setCategoria(categoria);
        productoCreated.setCreateAt(dateSystem);

        // Mocks & Stubs configuration
        when(productoService.save(any())).thenReturn(Mono.just(productoCreated));

        // Business logic execution
        client.post().uri("/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(productoToCreated))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().valueEquals(HttpHeaders.LOCATION, "/productos/abc123");
                /*.expectBody(Producto.class)
                .consumeWith(response -> {
                    Producto p = response.getResponseBody();
                    assertThat(p.getId()).isEqualTo("abc123");
                    assertThat(p.getNombre()).isEqualTo("producto1");
                    assertThat(p.getPrecio()).isEqualTo(1.5d);
                    assertThat(p.getCreateAt()).isEqualTo(dateSystem);
                    assertThat(p.getCategoria()).isEqualTo(categoria);
                });*/
        // Validating mocks behaviour
        //verify(productoService,times(1)).save(productoToCreated);
        //verifyNoMoreInteractions(productoService);

        // Validating results

    }


}
