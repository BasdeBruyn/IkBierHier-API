package nl.ikbierhier.integration;

import nl.ikbierhier.helpers.JsonHelper;
import nl.ikbierhier.models.Group;
import nl.ikbierhier.models.Location;
import nl.ikbierhier.models.User;
import nl.ikbierhier.repositories.GroupRepository;
import nl.ikbierhier.repositories.LocationRepository;
import nl.ikbierhier.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Timestamp;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LocationTest extends IntegrationTest {
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;

    private final Calendar calendar = Calendar.getInstance();

    @Test
    public void test_GET_location() throws Exception {
        Calendar pastCalendar = Calendar.getInstance();
        pastCalendar.add(Calendar.MINUTE, 10);
        Timestamp time = new Timestamp(pastCalendar.getTime().getTime());

        User user = userRepository.save(new User("facebook|12345678", "John"));
        Group group = groupRepository.save(new Group("group1", "", new ArrayList<>(), new ArrayList<>()));
        group.getUsers().add(user);
        groupRepository.save(group);
        Location location1 = new Location(group, user, 12.1, 12.2, time);
        Location location2 = new Location(group, user, 12.2, 12.3, time);
        List<Location> locations = Arrays.asList(location1, location2);

        locationRepository.saveAll(locations);

        mockMvc.perform(
                get("/location")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token")
        ).andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string(JsonHelper.toJsonString(locations)));
    }

    @Test
    public void test_GET_location_existingId() throws Exception {
        Timestamp now = new Timestamp(calendar.getTime().getTime());
        Location location = new Location(null, null, 12.1, 12.2, now);
        location = locationRepository.save(location);

        mockMvc.perform(
                get("/location/" + location.getUuid())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string(JsonHelper.toJsonString(location)));
    }

    @Test
    public void test_GET_location_nonExistingId() throws Exception {
        Timestamp now = new Timestamp(calendar.getTime().getTime());
        Location location = new Location(null, null, 12.1, 12.2, now);
        locationRepository.save(location);

        mockMvc.perform(
                get("/location/" + "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token")
        )
                .andExpect(status().isNotFound());
    }

    @Test
    public void test_POST_share() throws Exception {
        Timestamp now = new Timestamp(calendar.getTime().getTime());

        User user1 = new User("fakeId1", "John");
        User user2 = new User("fakeId2", "Jane");
        ArrayList<User> users = new ArrayList<>(Arrays.asList(user1, user2));

        Group group = groupRepository.save(new Group("Group1", "Description", users, null));
        groupRepository.save(group);

        Location oldLocation =
                locationRepository.save(new Location(group, user1, 12.12, 12.12, now));
        Location otherUsersLocation =
                locationRepository.save(new Location(group, user2, 0, 0, now));

        Location newLocation = new Location(group, user1, 12.1, 12.2, now);

        MvcResult result = mockMvc.perform(
                post("/location/share")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonHelper.toJsonString(newLocation)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        Location returnedLocation = JsonHelper.toObject(result.getResponse().getContentAsString(), Location.class);
        assertTrue(locationsAreEqual(newLocation, returnedLocation));

        Location savedLocation = locationRepository.findById(returnedLocation.getUuid()).orElseThrow(Exception::new);
        assertTrue(locationsAreEqual(newLocation, savedLocation));

        assertNull(locationRepository.findById(oldLocation.getUuid()).orElse(null));
        assertNotNull(locationRepository.findById(otherUsersLocation.getUuid()).orElse(null));
    }

    private boolean locationsAreEqual(Location location1, Location location2) {
        return Double.compare(location1.getLatitude(), location2.getLatitude()) == 0 &&
                Double.compare(location1.getLongitude(), location2.getLongitude()) == 0 &&
                Objects.equals(location1.getGroup().getUuid(), location2.getGroup().getUuid()) &&
                Objects.equals(location1.getUser().getId(), location2.getUser().getId()) &&
                Objects.equals(location1.getExpiresAt(), location2.getExpiresAt());
    }


    @Test
    public void test_DELETE_location_existingLocation() throws Exception {
        Timestamp now = new Timestamp(calendar.getTime().getTime());

        Location location = new Location(null, null, 12.1, 12.2, now);
        location = locationRepository.save(location);

        mockMvc.perform(delete("/location/delete" , location)
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonHelper.toJsonString(location)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();


        boolean hasResult = locationRepository.findAll().iterator().hasNext();
        assertThat(hasResult, is(false));

    }
}
