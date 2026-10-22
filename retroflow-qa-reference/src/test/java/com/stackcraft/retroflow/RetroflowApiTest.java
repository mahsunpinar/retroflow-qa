package com.stackcraft.retroflow;

import static org.assertj.core.api.Assertions.assertThat;

import com.stackcraft.retroflow.dto.CompleteActionItemRequest;
import com.stackcraft.retroflow.dto.CreateActionItemRequest;
import com.stackcraft.retroflow.dto.CreateFeedbackItemRequest;
import com.stackcraft.retroflow.dto.CreateRetrospectiveRequest;
import com.stackcraft.retroflow.dto.CreateTeamRequest;
import com.stackcraft.retroflow.entity.ActionItem;
import com.stackcraft.retroflow.entity.ActionItemPriority;
import com.stackcraft.retroflow.entity.FeedbackItem;
import com.stackcraft.retroflow.entity.FeedbackType;
import com.stackcraft.retroflow.entity.Retrospective;
import com.stackcraft.retroflow.entity.Team;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Inherited from the dev cohort — written at the end of a long day, under
 * time pressure, to prove the happy paths work before handover.
 *
 * Read it like evidence, not like a spec: a passing suite tells you these
 * specific requests behaved as expected. It does not tell you the app is
 * correct everywhere else.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RetroflowApiTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createTeam_returnsCreatedTeam() {
        CreateTeamRequest request = new CreateTeamRequest("Team Alpha", List.of("Alice", "Bob"));

        ResponseEntity<Team> response = restTemplate.postForEntity("/api/teams", request, Team.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Team Alpha");
    }

    @Test
    void createTeam_withMembers_returnsMembersList() {
        ResponseEntity<Team> response = restTemplate.postForEntity(
                "/api/teams", new CreateTeamRequest("Team Eta", List.of("Ivan", "Judy")), Team.class);

        assertThat(response.getBody().getMembers()).hasSize(2);
    }

    @Test
    void createRetrospective_succeeds() {
        Team team = createTeam("Team Beta", List.of("Carol", "Dave"));

        ResponseEntity<Retrospective> response = restTemplate.postForEntity(
                "/api/teams/" + team.getId() + "/retrospectives",
                new CreateRetrospectiveRequest("Sprint 1 Retro"),
                Retrospective.class);

        // Only checks the request succeeded, not which status code it used.
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void addFeedbackItem_toOpenRetrospective_succeeds() {
        Team team = createTeam("Team Gamma", List.of("Eve"));
        Retrospective retro = createRetrospective(team.getId(), "Sprint 1 Retro");

        ResponseEntity<FeedbackItem> response = restTemplate.postForEntity(
                "/api/retrospectives/" + retro.getId() + "/feedback-items",
                new CreateFeedbackItemRequest("Great sprint", FeedbackType.WENT_WELL, "Eve"),
                FeedbackItem.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void closeRetrospective_updatesStatus() {
        Team team = createTeam("Team Delta", List.of("Frank"));
        Retrospective retro = createRetrospective(team.getId(), "Sprint 1 Retro");

        ResponseEntity<Retrospective> response = restTemplate.exchange(
                "/api/retrospectives/" + retro.getId() + "/close",
                HttpMethod.PUT,
                null,
                Retrospective.class);

        // Doesn't check the actual status value came back as CLOSED.
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void addActionItem_andMarkComplete_succeeds() {
        Team team = createTeam("Team Epsilon", List.of("Grace"));
        Retrospective retro = createRetrospective(team.getId(), "Sprint 1 Retro");

        ResponseEntity<ActionItem> created = restTemplate.postForEntity(
                "/api/retrospectives/" + retro.getId() + "/action-items",
                new CreateActionItemRequest("Fix the build", ActionItemPriority.HIGH, "Grace"),
                ActionItem.class);
        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Long actionItemId = created.getBody().getId();
        ResponseEntity<ActionItem> completed = restTemplate.exchange(
                "/api/action-items/" + actionItemId + "/complete",
                HttpMethod.PATCH,
                new HttpEntity<>(new CompleteActionItemRequest(true)),
                ActionItem.class);

        assertThat(completed.getBody().isCompleted()).isTrue();
    }

    @Test
    void filterActionItems_byPriority_returnsResults() {
        Team team = createTeam("Team Zeta", List.of("Heidi"));
        Retrospective retro = createRetrospective(team.getId(), "Sprint 1 Retro");
        restTemplate.postForEntity(
                "/api/retrospectives/" + retro.getId() + "/action-items",
                new CreateActionItemRequest("High priority item", ActionItemPriority.HIGH, "Heidi"),
                ActionItem.class);
        restTemplate.postForEntity(
                "/api/retrospectives/" + retro.getId() + "/action-items",
                new CreateActionItemRequest("Low priority item", ActionItemPriority.LOW, "Heidi"),
                ActionItem.class);

        // Only filters on priority — doesn't combine with completed.
        ResponseEntity<ActionItem[]> response = restTemplate.getForEntity(
                "/api/action-items?priority=HIGH", ActionItem[].class);

        assertThat(response.getBody()).isNotNull();
    }

    private Team createTeam(String name, List<String> members) {
        return restTemplate.postForEntity("/api/teams", new CreateTeamRequest(name, members), Team.class).getBody();
    }

    private Retrospective createRetrospective(Long teamId, String title) {
        return restTemplate.postForEntity(
                "/api/teams/" + teamId + "/retrospectives",
                new CreateRetrospectiveRequest(title),
                Retrospective.class).getBody();
    }

}
