package com.example.rongsok.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="user")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nama;

    private String npm;

    private String kelas;



    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getnama() {
        return nama;
    }

    public void setnama(String nama) {
        this.nama = nama;
    }
    public String getnpm() {
        return npm;
    }

    public void setnpm(String npm) {
        this.npm = npm;
    }
    public String getkelas() {
        return kelas;
    }

    public void setkelas(String kelas) {
        this.kelas = kelas;
    }

}