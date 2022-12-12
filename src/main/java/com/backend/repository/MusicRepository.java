package com.backend.repository;

import com.backend.entity.Music;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MusicRepository implements PanacheRepository<Music> {
}
