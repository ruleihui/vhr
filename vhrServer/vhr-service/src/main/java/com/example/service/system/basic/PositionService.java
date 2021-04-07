package com.example.service.system.basic;

import com.example.dao.system.basic.PositionDao;
import com.example.entity.Position;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PositionService {
    PositionDao positionDao;

    public PositionService(PositionDao positionDao) {
        this.positionDao = positionDao;
    }

    public List<Position> getAll() {
        return positionDao.getAll();
    }

    public int addPosition(Position position) {
        position.setEnabled(true);
        position.setCreateDate(new Date());
        return positionDao.insert(position);
    }

    public int updateById(Position position) {
        return positionDao.update(position);
    }

    public int deleteById(Integer id) {
        return positionDao.deleteById(id);
    }

    public int deleteByIds(Integer[] ids) {
        return positionDao.deleteByIds(ids);
    }
}
