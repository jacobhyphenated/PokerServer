package com.hyphenated.card.dao;

import org.springframework.stereotype.Repository;

import com.hyphenated.card.domain.HandEntity;

@Repository
public class HandDaoImpl extends BaseDaoImpl<HandEntity> implements HandDao{

//	@Override
//	public HandEntity save(HandEntity hand){
//		//Remove the saved cards / deck for this hand.  The save will add them back in
//		//This servs to prevent duplicates (open issue with Hibernate / JPA collection delete/updates)
//		getSession().createSQLQuery("delete from hand_deck where hand_id = :handid").
//			setLong("handid", hand.getId()).
//			executeUpdate();
//		return super.save(hand);
//	}
}
