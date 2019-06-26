package ru.bmstu.coursework.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.bmstu.coursework.domain.enums.Category;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    @NotEmpty(message = "*Please provide a title")
    private String title;

    @Column(name = "description")
    @NotEmpty(message = "*Please provide a description")
    private String description;

    @Column(name = "price")
    @NotNull(message = "*Please provide a price")
    private Double price;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "*Please provide your category")
    private Category category;

    @OneToMany(mappedBy = "task", cascade = CascadeType.REMOVE)
    private List<Message> messages;

    @OneToMany(mappedBy = "task", cascade = CascadeType.REMOVE)
    private Set<Order> orders;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
