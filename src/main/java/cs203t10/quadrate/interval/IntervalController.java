package cs203t10.quadrate.interval;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interval")
public class IntervalController {
    private final IntervalService intervalService;

    @PostMapping
    public Interval createInterval(@RequestBody Interval interval) {
        return intervalService.createInterval(interval);
    }

    @GetMapping
    public List<Interval> getAllIntervals() {
        return intervalService.getAllIntervals();
    }

    @GetMapping("/type/{type}")
    public List<Interval> getIntervalsByType(@PathVariable("type") String type) {
        return intervalService.getIntervalsByType(type);
    }

    @GetMapping("/{id}")
    public Interval getInterval(@PathVariable("id") Long id) {
        return intervalService.getInterval(id);
    }

    @PutMapping("/{id}")
    public Interval updateInterval(@PathVariable("id") Long id, @RequestBody Interval interval) {
        return intervalService.updateInterval(id, interval);
    }

    @DeleteMapping("/{id}")
    public Interval removeInterval(@PathVariable("id") Long id) {
        return intervalService.removeInterval(id);
    }
}
