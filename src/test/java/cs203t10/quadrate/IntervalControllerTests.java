package cs203t10.quadrate;

import com.fasterxml.jackson.databind.ObjectMapper;
import cs203t10.quadrate.interval.Interval;
import cs203t10.quadrate.interval.IntervalController;
import cs203t10.quadrate.interval.IntervalService;
import cs203t10.quadrate.user.User;
import cs203t10.quadrate.location.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.sql.Timestamp;
import java.util.Calendar;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IntervalController.class)
public class IntervalControllerTests {

    @MockBean
    private IntervalService intervalService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createInterval_Ok() throws Exception {
        Interval input = new Interval(null, getStartTime(), getEndTime(), "Booking", getUser(), getLocation());
        Interval output = new Interval(1L, getStartTime(), getEndTime(), "Booking", getUser(), getLocation());

        when(intervalService.createInterval(any(Interval.class))).thenAnswer(i -> {
            Interval interval = i.getArgument(0, Interval.class);
            interval.setId(1L);
            return interval;
        });

        // System.out.println(getStartTime());
        // System.out.println("===============================CREATE===============================");
        // System.out.println(output);

        mockMvc.perform(post("/api/interval").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(input))).andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(output)));
    }

    @Test
    void getAllIntervals_Ok() throws Exception {
        List<Interval> intervals = List.of(
                new Interval(getStartTime(), getEndTime(), "Booking", getUser(), getLocation()),
                new Interval(getStartTime(), getEndTime(), "Booking", getUser(), getLocation()));

        when(intervalService.getAllIntervals()).thenReturn(intervals);

        mockMvc.perform(get("/api/interval").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(intervals)));
    }

    @Test
    void getInterval_Ok() throws Exception {
        Interval interval = new Interval(getStartTime(), getEndTime(), "Booking", getUser(), getLocation());

        when(intervalService.getInterval(any(Long.class))).thenReturn(interval);

        mockMvc.perform(get("/api/interval/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(interval)));
    }

    @Test
    void updateInterval_Ok() throws Exception {
        Interval oldInterval = new Interval(1L, getStartTime(), getEndTime(), "Booking", getUser(), getLocation());
        Interval newInterval = new Interval(1L, getStartTime(), getNewEndTime(), "Booking", getUser(), getLocation());

        when(intervalService.updateInterval(any(Long.class), any(Interval.class))).thenAnswer(i -> {
            Interval interval = i.getArgument(1, Interval.class);
            interval.setEndTime(getNewEndTime());
            return interval;
        });

        // System.out.println(getStartTime());
        // System.out.println("===============================UPDATE===============================");
        // System.out.println(newInterval);

        mockMvc.perform(put("/api/interval/1").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(oldInterval))).andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(newInterval)));
    }

    @Test
    void deleteInterval_OK() throws Exception {
        Interval interval = new Interval(1L, getStartTime(), getEndTime(), "Booking", getUser(), getLocation());

        when(intervalService.removeInterval(any(Long.class))).thenReturn(interval);

        mockMvc.perform(delete("/api/interval/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(interval)));
    }

    private Timestamp getStartTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(2021, 10, 21);
        cal.set(Calendar.HOUR_OF_DAY, 12);
        cal.set(Calendar.MINUTE, 30);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new Timestamp(cal.getTimeInMillis());
    }

    private Timestamp getEndTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(2021, 10, 21);
        cal.set(Calendar.HOUR_OF_DAY, 16);
        cal.set(Calendar.MINUTE, 30);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new Timestamp(cal.getTimeInMillis());
    }

    private Timestamp getNewEndTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(2021, 10, 21);
        cal.set(Calendar.HOUR_OF_DAY, 18);
        cal.set(Calendar.MINUTE, 30);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new Timestamp(cal.getTimeInMillis());
    }

    private User getUser() {
        return new User("YuXuan");
    }

    private Location getLocation() {
        return new Location("Desk 1", 1, true);
    }
}
