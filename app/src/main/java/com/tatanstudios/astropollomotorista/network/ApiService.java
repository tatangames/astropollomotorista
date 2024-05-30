package com.tatanstudios.astropollomotorista.network;

import com.tatanstudios.astropollomotorista.modelos.ordenes.ModeloOrdenes;
import com.tatanstudios.astropollomotorista.modelos.productos.ModeloProducto;
import com.tatanstudios.astropollomotorista.modelos.usuario.AccessTokenUser;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {


    @POST("motorista/login")
    @FormUrlEncoded
    Observable<AccessTokenUser> inicioSesion(@Field("usuario") String usuario,
                                             @Field("password") String password,
                                             @Field("idfirebase") String idfirebase);


    // LISTADO DE NUEVAS ORDENES PARA MOTORISTAS

    @FormUrlEncoded
    @POST("motorista/nuevas/ordenes")
    Observable<ModeloOrdenes> verNuevaOrdenesMotorista(@Field("id") String id,
                                                       @Field("idfirebase") String idfirebase);



    // LISTADO DE PRODUCTOS DE LA ORDEN
    @FormUrlEncoded
    @POST("motorista/listado/producto/orden")
    Observable<ModeloProducto> verListadoDeProductosOrden(@Field("idorden") int idorden);


    // SELECCIONAR UNA ORDEN
    @FormUrlEncoded
    @POST("motorista/seleccionar/orden")
    Observable<AccessTokenUser> seleccionarOrden(@Field("idorden") int idorden,
                                                 @Field("id") String id);



    // LISTADO DE ORDENES QUE SE VAN A ENTREGAR
    @FormUrlEncoded
    @POST("motorista/pendientes/entrega/orden")
    Observable<ModeloOrdenes> verOrdenesPendientes(@Field("id") String id);


    @FormUrlEncoded
    @POST("motorista/iniciar/entrega/orden")
    Observable<AccessTokenUser> iniciarEntregaOrden(@Field("idorden") int idorden);


    // LISTADO DE ORDENES QUE SE ESTAN ENTREGANDO
    @FormUrlEncoded
    @POST("motorista/entregando/entrega/orden")
    Observable<ModeloOrdenes> verOrdenesEntregando(@Field("id") String id);


    // FINALIZAR ORDEN POR PARTE DEL MOTORISTA
    @FormUrlEncoded
    @POST("motorista/finalizar/entrega/orden")
    Observable<AccessTokenUser> finalizarEntrega(@Field("idorden") int idorden);


    // LISTADO DE ORDENES COMPLETADAS HOY MOTORISTA
    @FormUrlEncoded
    @POST("motorista/listado/completadas/hoy/orden")
    Observable<ModeloOrdenes> listadoOrdenesCompletadasHoy(@Field("id") String id);


    // LISTADO DE ORDENES CANCELADAS HOY MOTORISTA
    @FormUrlEncoded
    @POST("motorista/listado/canceladas/hoy/orden")
    Observable<ModeloOrdenes> listadoOrdenesCanceladasHoy(@Field("id") String id);



    // HISTORIAL DE ORDENES POR FECHA
    @FormUrlEncoded
    @POST("motorista/historial/ordenes")
    Observable<ModeloOrdenes> informacionHistorial(@Field("id") String id,
                                                   @Field("fecha1") String fecha1,
                                                   @Field("fecha2") String fecha2);

    // INFORMACION DE NOTIFICACIONES
    @FormUrlEncoded
    @POST("motorista/opcion/notificacion")
    Observable<AccessTokenUser> verOpcionNotificaciones(
            @Field("id") String id);



    // EDITAR ESTADO DE NOTIFICACIONES
    @FormUrlEncoded
    @POST("motorista/opcion/notificacion/editar")
    Observable<AccessTokenUser> editarEstadoNotificaciones(
            @Field("id") String id, @Field("disponible") int disponible);








}
