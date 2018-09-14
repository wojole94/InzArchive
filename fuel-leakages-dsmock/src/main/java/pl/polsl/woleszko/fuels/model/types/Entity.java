package pl.polsl.woleszko.fuels.model.types;

import java.io.Serializable;
import java.util.Date;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;

import lombok.Getter;
@Getter
@CsvRecord(separator = ";")
public abstract class Entity implements Comparable<Entity>, Serializable {
    
	private static final long serialVersionUID = 1L;
	private static Integer globalRowNum = 0;
	public abstract Date getDate();
	public Integer rowNum = new Integer(0);
	
	Entity(){
		rowNum = ++globalRowNum;
	}
	
    public int compareTo(Entity o) {
        return this.rowNum.compareTo(o.rowNum);
    }
}
