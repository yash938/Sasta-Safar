package com.Ecommerce.store.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder

public class Categories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryId;

    @Column(length = 40,nullable = false)
    private String title;
    @Column(length = 50)
    private String description;
    private String coverImage;

    @OneToMany(mappedBy = "categories",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

}
