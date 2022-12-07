package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.acme.entity.Music;
import org.acme.entity.User;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MusicRepository implements PanacheRepository<Music> {
}
