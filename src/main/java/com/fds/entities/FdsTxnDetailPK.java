package com.fds.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the FDS_TXN_DETAIL database table.
 * 
 */
@Embeddable
public class FdsTxnDetailPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="PX_OA008_PAN", unique=true, nullable=false, length=19)
	private String pxOa008Pan;

	@Column(name="P9_OA008_SEQ", unique=true, nullable=false, precision=6)
	private long p9Oa008Seq;

	public FdsTxnDetailPK() {
	}
	public String getPxOa008Pan() {
		return this.pxOa008Pan;
	}
	public void setPxOa008Pan(String pxOa008Pan) {
		this.pxOa008Pan = pxOa008Pan;
	}
	public long getP9Oa008Seq() {
		return this.p9Oa008Seq;
	}
	public void setP9Oa008Seq(long p9Oa008Seq) {
		this.p9Oa008Seq = p9Oa008Seq;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof FdsTxnDetailPK)) {
			return false;
		}
		FdsTxnDetailPK castOther = (FdsTxnDetailPK)other;
		return 
			this.pxOa008Pan.equals(castOther.pxOa008Pan)
			&& (this.p9Oa008Seq == castOther.p9Oa008Seq);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.pxOa008Pan.hashCode();
		hash = hash * prime + ((int) (this.p9Oa008Seq ^ (this.p9Oa008Seq >>> 32)));
		
		return hash;
	}
}