package utils;

import model.Product;
import model.ResponseObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class RestClientUtil {



    public static ResponseObject getSingleResource(String uri, String path) {
        Client client = ClientBuilder.newClient();
        ResponseObject responseObject=client
                .target(uri)
                .path(path)
                .request(MediaType.APPLICATION_JSON)
                .get(ResponseObject.class);
        return responseObject;
    }

    public static ResponseObject getResourceList(String uri){
        Client client = ClientBuilder.newClient();
        ResponseObject responseObject=client
                .target(uri)
                .request(MediaType.APPLICATION_JSON)
                .get(ResponseObject.class);
        return responseObject;
    }

    public static void updateResource(String uri, int id, Object resource){
        Client client=ClientBuilder.newClient();
        Response response=client
                .target(uri)
                .path(String.valueOf(id))
                .request()
                .put(Entity.entity(resource,MediaType.APPLICATION_JSON));
    }

    public static ResponseObject addNewResource(Object resource,String uri){
        Client client = ClientBuilder.newClient();
        Response response=client
                .target(uri)
                .request(MediaType.APPLICATION_JSON)
                .post((Entity.entity(resource,MediaType.APPLICATION_JSON)));
        ResponseObject responseObject= response.readEntity(ResponseObject.class);
        return responseObject;
    }

    public static void deleteResource(String uri, int id) {
        Client client = ClientBuilder.newClient();
        client
                .target(uri)
                .path(String.valueOf(id))
                .request()
                .delete();
    }
}
