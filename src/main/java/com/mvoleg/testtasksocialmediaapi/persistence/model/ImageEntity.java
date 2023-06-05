package com.mvoleg.testtasksocialmediaapi.persistence.model;

import jakarta.persistence.*;

@Entity
@Table(name = "image")
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "data", nullable = false, columnDefinition = "LONGBLOB")
    private byte[] imageData;

    @Column(name = "size")
    private Long size;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private PostEntity post;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public PostEntity getPost() {
        return post;
    }

    public void setPost(PostEntity postEntity) {
        this.post = postEntity;
    }
}
