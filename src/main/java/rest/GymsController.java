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

import java.net.URI;
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
        return gyms.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        Gym gym = em.find(Gym.class, id);

        if (gym == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(toDTO(gym)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(GymDTO gymDTO) {
        Gym gym = new Gym();
        gym.setName(gymDTO.getName());

        em.persist(gym);
        em.flush();

        return Response
                .created(URI.create("/gyms/" + gym.getId()))
                .entity(toDTO(gym))
                .build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response update(@PathParam("id") Long id, GymDTO gymDTO) {
        Gym gym = em.find(Gym.class, id);

        if (gym == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        gym.setName(gymDTO.getName());
        em.flush();

        return Response.ok(toDTO(gym)).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        Gym gym = em.find(Gym.class, id);

        if (gym == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Gym not found with id: " + id)
                    .build();
        }

        try {
            em.remove(gym);
            em.flush();

            return Response.ok("Gym deleted successfully. Deleted gym id: " + id).build();
        } catch (Exception exception) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Could not delete gym with id " + id + ". It may still have related trainers.")
                    .build();
        }
    }

    private GymDTO toDTO(Gym gym) {
        GymDTO dto = new GymDTO();
        dto.setId(gym.getId());
        dto.setName(gym.getName());
        return dto;
    }
}