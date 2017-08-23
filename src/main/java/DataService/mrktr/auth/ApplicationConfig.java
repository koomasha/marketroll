/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataService.mrktr.auth;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author KooMasha
 */
@javax.ws.rs.ApplicationPath("mrktr")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        addResources(resources);
        return resources;
    }


    private void addResources(Set<Class<?>> resources) {
        resources.add(DataService.mrktr.auth.Secured.class);
        resources.add(DataService.mrktr.auth.AuthUserPrincipal.class);
        resources.add(DataService.mrktr.BaseResource.class);
    }

    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(DataService.mrktr.AuthenticationResource.class);
        resources.add(DataService.mrktr.UserResource.class);
        resources.add(DataService.mrktr.auth.AuthenticationFilter.class);
    }

}
