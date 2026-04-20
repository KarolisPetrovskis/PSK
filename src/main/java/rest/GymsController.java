package rest;

import entities.Gym;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import rest.contracts.GymDTO;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Path("/gyms")
public class GymsController {

    @Inject
    private EntityManager em;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<GymDTO> getGyms() {
        List<Gym> gyms = em.createQuery("select g from Gym g", Gym.class).getResultList();
        return gyms.stream().map(g -> {
            GymDTO dto = new GymDTO();
            dto.setName(g.getName());
            return dto;
        }).collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        Gym gym = em.find(Gym.class, id);
        if (gym == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        GymDTO dto = new GymDTO();
        dto.setName(gym.getName());
        return Response.ok(dto).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(GymDTO gymDTO) {
        Gym gym = new Gym();
        gym.setName(gymDTO.getName());
        em.persist(gym);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response update(@PathParam("id") Long id, GymDTO gymDTO) {
        Gym gym = em.find(Gym.class, id);
        if (gym == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        gym.setName(gymDTO.getName());
        em.merge(gym);
        return Response.ok().build();
    }
}