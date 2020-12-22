package com.dd.web.rest;

import com.dd.ClassscheduleApp;
import com.dd.domain.Notification;
import com.dd.domain.User;
import com.dd.repository.NotificationRepository;
import com.dd.service.NotificationService;
import com.dd.service.dto.NotificationCriteria;
import com.dd.service.NotificationQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link NotificationResource} REST controller.
 */
@SpringBootTest(classes = ClassscheduleApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class NotificationResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_CHANNEL = "AAAAAAAAAA";
    private static final String UPDATED_CHANNEL = "BBBBBBBBBB";

    private static final String DEFAULT_RECIPIENT = "AAAAAAAAAA";
    private static final String UPDATED_RECIPIENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_SEND_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SEND_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationQueryService notificationQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNotificationMockMvc;

    private Notification notification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notification createEntity(EntityManager em) {
        Notification notification = new Notification()
            .type(DEFAULT_TYPE)
            .channel(DEFAULT_CHANNEL)
            .recipient(DEFAULT_RECIPIENT)
            .sendDate(DEFAULT_SEND_DATE);
        return notification;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notification createUpdatedEntity(EntityManager em) {
        Notification notification = new Notification()
            .type(UPDATED_TYPE)
            .channel(UPDATED_CHANNEL)
            .recipient(UPDATED_RECIPIENT)
            .sendDate(UPDATED_SEND_DATE);
        return notification;
    }

    @BeforeEach
    public void initTest() {
        notification = createEntity(em);
    }

    @Test
    @Transactional
    public void createNotification() throws Exception {
        int databaseSizeBeforeCreate = notificationRepository.findAll().size();
        // Create the Notification
        restNotificationMockMvc.perform(post("/api/notifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notification)))
            .andExpect(status().isCreated());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeCreate + 1);
        Notification testNotification = notificationList.get(notificationList.size() - 1);
        assertThat(testNotification.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testNotification.getChannel()).isEqualTo(DEFAULT_CHANNEL);
        assertThat(testNotification.getRecipient()).isEqualTo(DEFAULT_RECIPIENT);
        assertThat(testNotification.getSendDate()).isEqualTo(DEFAULT_SEND_DATE);
    }

    @Test
    @Transactional
    public void createNotificationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = notificationRepository.findAll().size();

        // Create the Notification with an existing ID
        notification.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificationMockMvc.perform(post("/api/notifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notification)))
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllNotifications() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList
        restNotificationMockMvc.perform(get("/api/notifications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notification.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].channel").value(hasItem(DEFAULT_CHANNEL)))
            .andExpect(jsonPath("$.[*].recipient").value(hasItem(DEFAULT_RECIPIENT)))
            .andExpect(jsonPath("$.[*].sendDate").value(hasItem(DEFAULT_SEND_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get the notification
        restNotificationMockMvc.perform(get("/api/notifications/{id}", notification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notification.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.channel").value(DEFAULT_CHANNEL))
            .andExpect(jsonPath("$.recipient").value(DEFAULT_RECIPIENT))
            .andExpect(jsonPath("$.sendDate").value(DEFAULT_SEND_DATE.toString()));
    }


    @Test
    @Transactional
    public void getNotificationsByIdFiltering() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        Long id = notification.getId();

        defaultNotificationShouldBeFound("id.equals=" + id);
        defaultNotificationShouldNotBeFound("id.notEquals=" + id);

        defaultNotificationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNotificationShouldNotBeFound("id.greaterThan=" + id);

        defaultNotificationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNotificationShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllNotificationsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where type equals to DEFAULT_TYPE
        defaultNotificationShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the notificationList where type equals to UPDATED_TYPE
        defaultNotificationShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllNotificationsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where type not equals to DEFAULT_TYPE
        defaultNotificationShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the notificationList where type not equals to UPDATED_TYPE
        defaultNotificationShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllNotificationsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultNotificationShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the notificationList where type equals to UPDATED_TYPE
        defaultNotificationShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllNotificationsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where type is not null
        defaultNotificationShouldBeFound("type.specified=true");

        // Get all the notificationList where type is null
        defaultNotificationShouldNotBeFound("type.specified=false");
    }
                @Test
    @Transactional
    public void getAllNotificationsByTypeContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where type contains DEFAULT_TYPE
        defaultNotificationShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the notificationList where type contains UPDATED_TYPE
        defaultNotificationShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllNotificationsByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where type does not contain DEFAULT_TYPE
        defaultNotificationShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the notificationList where type does not contain UPDATED_TYPE
        defaultNotificationShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }


    @Test
    @Transactional
    public void getAllNotificationsByChannelIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where channel equals to DEFAULT_CHANNEL
        defaultNotificationShouldBeFound("channel.equals=" + DEFAULT_CHANNEL);

        // Get all the notificationList where channel equals to UPDATED_CHANNEL
        defaultNotificationShouldNotBeFound("channel.equals=" + UPDATED_CHANNEL);
    }

    @Test
    @Transactional
    public void getAllNotificationsByChannelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where channel not equals to DEFAULT_CHANNEL
        defaultNotificationShouldNotBeFound("channel.notEquals=" + DEFAULT_CHANNEL);

        // Get all the notificationList where channel not equals to UPDATED_CHANNEL
        defaultNotificationShouldBeFound("channel.notEquals=" + UPDATED_CHANNEL);
    }

    @Test
    @Transactional
    public void getAllNotificationsByChannelIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where channel in DEFAULT_CHANNEL or UPDATED_CHANNEL
        defaultNotificationShouldBeFound("channel.in=" + DEFAULT_CHANNEL + "," + UPDATED_CHANNEL);

        // Get all the notificationList where channel equals to UPDATED_CHANNEL
        defaultNotificationShouldNotBeFound("channel.in=" + UPDATED_CHANNEL);
    }

    @Test
    @Transactional
    public void getAllNotificationsByChannelIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where channel is not null
        defaultNotificationShouldBeFound("channel.specified=true");

        // Get all the notificationList where channel is null
        defaultNotificationShouldNotBeFound("channel.specified=false");
    }
                @Test
    @Transactional
    public void getAllNotificationsByChannelContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where channel contains DEFAULT_CHANNEL
        defaultNotificationShouldBeFound("channel.contains=" + DEFAULT_CHANNEL);

        // Get all the notificationList where channel contains UPDATED_CHANNEL
        defaultNotificationShouldNotBeFound("channel.contains=" + UPDATED_CHANNEL);
    }

    @Test
    @Transactional
    public void getAllNotificationsByChannelNotContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where channel does not contain DEFAULT_CHANNEL
        defaultNotificationShouldNotBeFound("channel.doesNotContain=" + DEFAULT_CHANNEL);

        // Get all the notificationList where channel does not contain UPDATED_CHANNEL
        defaultNotificationShouldBeFound("channel.doesNotContain=" + UPDATED_CHANNEL);
    }


    @Test
    @Transactional
    public void getAllNotificationsByRecipientIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where recipient equals to DEFAULT_RECIPIENT
        defaultNotificationShouldBeFound("recipient.equals=" + DEFAULT_RECIPIENT);

        // Get all the notificationList where recipient equals to UPDATED_RECIPIENT
        defaultNotificationShouldNotBeFound("recipient.equals=" + UPDATED_RECIPIENT);
    }

    @Test
    @Transactional
    public void getAllNotificationsByRecipientIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where recipient not equals to DEFAULT_RECIPIENT
        defaultNotificationShouldNotBeFound("recipient.notEquals=" + DEFAULT_RECIPIENT);

        // Get all the notificationList where recipient not equals to UPDATED_RECIPIENT
        defaultNotificationShouldBeFound("recipient.notEquals=" + UPDATED_RECIPIENT);
    }

    @Test
    @Transactional
    public void getAllNotificationsByRecipientIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where recipient in DEFAULT_RECIPIENT or UPDATED_RECIPIENT
        defaultNotificationShouldBeFound("recipient.in=" + DEFAULT_RECIPIENT + "," + UPDATED_RECIPIENT);

        // Get all the notificationList where recipient equals to UPDATED_RECIPIENT
        defaultNotificationShouldNotBeFound("recipient.in=" + UPDATED_RECIPIENT);
    }

    @Test
    @Transactional
    public void getAllNotificationsByRecipientIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where recipient is not null
        defaultNotificationShouldBeFound("recipient.specified=true");

        // Get all the notificationList where recipient is null
        defaultNotificationShouldNotBeFound("recipient.specified=false");
    }
                @Test
    @Transactional
    public void getAllNotificationsByRecipientContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where recipient contains DEFAULT_RECIPIENT
        defaultNotificationShouldBeFound("recipient.contains=" + DEFAULT_RECIPIENT);

        // Get all the notificationList where recipient contains UPDATED_RECIPIENT
        defaultNotificationShouldNotBeFound("recipient.contains=" + UPDATED_RECIPIENT);
    }

    @Test
    @Transactional
    public void getAllNotificationsByRecipientNotContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where recipient does not contain DEFAULT_RECIPIENT
        defaultNotificationShouldNotBeFound("recipient.doesNotContain=" + DEFAULT_RECIPIENT);

        // Get all the notificationList where recipient does not contain UPDATED_RECIPIENT
        defaultNotificationShouldBeFound("recipient.doesNotContain=" + UPDATED_RECIPIENT);
    }


    @Test
    @Transactional
    public void getAllNotificationsBySendDateIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where sendDate equals to DEFAULT_SEND_DATE
        defaultNotificationShouldBeFound("sendDate.equals=" + DEFAULT_SEND_DATE);

        // Get all the notificationList where sendDate equals to UPDATED_SEND_DATE
        defaultNotificationShouldNotBeFound("sendDate.equals=" + UPDATED_SEND_DATE);
    }

    @Test
    @Transactional
    public void getAllNotificationsBySendDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where sendDate not equals to DEFAULT_SEND_DATE
        defaultNotificationShouldNotBeFound("sendDate.notEquals=" + DEFAULT_SEND_DATE);

        // Get all the notificationList where sendDate not equals to UPDATED_SEND_DATE
        defaultNotificationShouldBeFound("sendDate.notEquals=" + UPDATED_SEND_DATE);
    }

    @Test
    @Transactional
    public void getAllNotificationsBySendDateIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where sendDate in DEFAULT_SEND_DATE or UPDATED_SEND_DATE
        defaultNotificationShouldBeFound("sendDate.in=" + DEFAULT_SEND_DATE + "," + UPDATED_SEND_DATE);

        // Get all the notificationList where sendDate equals to UPDATED_SEND_DATE
        defaultNotificationShouldNotBeFound("sendDate.in=" + UPDATED_SEND_DATE);
    }

    @Test
    @Transactional
    public void getAllNotificationsBySendDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where sendDate is not null
        defaultNotificationShouldBeFound("sendDate.specified=true");

        // Get all the notificationList where sendDate is null
        defaultNotificationShouldNotBeFound("sendDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotificationsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        notification.setUser(user);
        notificationRepository.saveAndFlush(notification);
        Long userId = user.getId();

        // Get all the notificationList where user equals to userId
        defaultNotificationShouldBeFound("userId.equals=" + userId);

        // Get all the notificationList where user equals to userId + 1
        defaultNotificationShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNotificationShouldBeFound(String filter) throws Exception {
        restNotificationMockMvc.perform(get("/api/notifications?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notification.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].channel").value(hasItem(DEFAULT_CHANNEL)))
            .andExpect(jsonPath("$.[*].recipient").value(hasItem(DEFAULT_RECIPIENT)))
            .andExpect(jsonPath("$.[*].sendDate").value(hasItem(DEFAULT_SEND_DATE.toString())));

        // Check, that the count call also returns 1
        restNotificationMockMvc.perform(get("/api/notifications/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNotificationShouldNotBeFound(String filter) throws Exception {
        restNotificationMockMvc.perform(get("/api/notifications?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNotificationMockMvc.perform(get("/api/notifications/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingNotification() throws Exception {
        // Get the notification
        restNotificationMockMvc.perform(get("/api/notifications/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNotification() throws Exception {
        // Initialize the database
        notificationService.save(notification);

        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();

        // Update the notification
        Notification updatedNotification = notificationRepository.findById(notification.getId()).get();
        // Disconnect from session so that the updates on updatedNotification are not directly saved in db
        em.detach(updatedNotification);
        updatedNotification
            .type(UPDATED_TYPE)
            .channel(UPDATED_CHANNEL)
            .recipient(UPDATED_RECIPIENT)
            .sendDate(UPDATED_SEND_DATE);

        restNotificationMockMvc.perform(put("/api/notifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedNotification)))
            .andExpect(status().isOk());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
        Notification testNotification = notificationList.get(notificationList.size() - 1);
        assertThat(testNotification.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testNotification.getChannel()).isEqualTo(UPDATED_CHANNEL);
        assertThat(testNotification.getRecipient()).isEqualTo(UPDATED_RECIPIENT);
        assertThat(testNotification.getSendDate()).isEqualTo(UPDATED_SEND_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationMockMvc.perform(put("/api/notifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notification)))
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteNotification() throws Exception {
        // Initialize the database
        notificationService.save(notification);

        int databaseSizeBeforeDelete = notificationRepository.findAll().size();

        // Delete the notification
        restNotificationMockMvc.perform(delete("/api/notifications/{id}", notification.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
