/*
The MIT License (MIT)

Copyright (c) 2013 Jacob Kanipe-Illig

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package com.hyphenated.card.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "game_structure")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class GameStructure implements Serializable {

	private static final long serialVersionUID = 3663515999002547153L;
	private long id;
	private Date puaseStartTime;
	private int startingChips;

	@Column(name = "game_structure_id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "starting_chips")
	public int getStartingChips() {
		return startingChips;
	}

	public void setStartingChips(int startingChips) {
		this.startingChips = startingChips;
	}

	@Column(name = "pause_start_time")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getPuaseStartTime() {
		return puaseStartTime;
	}

	public void setPuaseStartTime(Date puaseStartTime) {
		this.puaseStartTime = puaseStartTime;
	}

}
