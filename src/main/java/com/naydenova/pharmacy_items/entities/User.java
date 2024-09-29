package com.naydenova.pharmacy_items.entities;

import com.naydenova.pharmacy_items.exceptions.AppException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name", nullable = false)
    @Size(max = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @Size(max = 100)
    private String lastName;

    @Column(nullable = false)
    @Size(max = 100)
    private String login;

    @Column(nullable = false)
    @Size(max = 100)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "app_user_item_mapping", joinColumns = @JoinColumn(name = "app_user_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private Set<Item> favorites = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Set<Search> searches = new HashSet<>();

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Item> getFavorites() {
        return favorites;
    }

    public void setFavorites(Set<Item> favorites) {
        this.favorites = favorites;
    }

    public Set<Search> getSearches() {
        return searches;
    }

    public void setSearches(Set<Search> searches) {
        this.searches = searches;
    }

    public boolean addToFavorites(Item item) {
        return this.favorites.add(item);
    }

    public boolean addToSearches(Search search) {
        return this.searches.add(search);
    }

    public void removeSearch(Long id) {
        final Search searchToDelete = this.searches.stream().filter(search -> search.getId().equals(id))
                .findAny()
                .orElseThrow(() -> new AppException("The search isn't present in favorite collection!", HttpStatus.NOT_FOUND));
        this.favorites.remove(searchToDelete);
    }

    public void removeItemFromFavorites(Long id) {
        final Item itemToDelete = this.favorites.stream().filter(item -> item.getId().equals(id))
                .findAny()
                .orElseThrow(() -> new AppException("The search isn't present in favorite collection!", HttpStatus.NOT_FOUND));
        this.favorites.remove(itemToDelete);
    }
}
