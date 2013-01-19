package com.hyphenated.card.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hyphenated.card.dao.BoardDao;
import com.hyphenated.card.domain.BoardEntity;

@Service
public class PokerHandServiceImpl implements PokerHandService {

	@Autowired
	private BoardDao boardDao;
	
	@Override
	@Transactional(readOnly=true)
	public BoardEntity getBoard(long id) {
		return boardDao.findById(id);
	}

	@Override
	@Transactional
	public BoardEntity saveBoard(BoardEntity board) {
		return boardDao.save(board);
	}

	@Override
	@Transactional(readOnly=true)
	public List<BoardEntity> getAllBoards() {
		return boardDao.findAll();
	}

	@Override
	@Transactional
	public void deleteBoard(BoardEntity board) {
		boardDao.remove(board);
	}

}
