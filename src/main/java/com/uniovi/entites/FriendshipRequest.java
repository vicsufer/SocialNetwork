package com.uniovi.entites;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "TFriendshipRequest", 
uniqueConstraints = { @UniqueConstraint(columnNames = 
					{ "sender_id", "receiver_id" }) 
})
public class FriendshipRequest {
	
	@Id
	@GeneratedValue
	Long id;
	boolean state;

	@ManyToOne
	@JoinColumn(name = "sender_id")
	User sender;
	
	@ManyToOne
	@JoinColumn(name = "receiver_id")
	User receiver;

	private FriendshipRequest(){}
	
	public FriendshipRequest(User sender, User receiver) {
		this.sender = sender;
		this.receiver = receiver;
		state = false;
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "FriendshipRequest [state=" + state + ", sender=" + sender + ", receiver=" + receiver + "]";
	}
	
}
