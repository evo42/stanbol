package org.apache.stanbol.entityhub.yard.solr.impl.constraintEncoders;

import java.util.Arrays;
import java.util.Collection;

import org.apache.stanbol.entityhub.yard.solr.model.IndexValue;
import org.apache.stanbol.entityhub.yard.solr.model.IndexValueFactory;
import org.apache.stanbol.entityhub.yard.solr.query.ConstraintTypePosition;
import org.apache.stanbol.entityhub.yard.solr.query.EncodedConstraintParts;
import org.apache.stanbol.entityhub.yard.solr.query.IndexConstraintTypeEncoder;
import org.apache.stanbol.entityhub.yard.solr.query.IndexConstraintTypeEnum;
import org.apache.stanbol.entityhub.yard.solr.query.ConstraintTypePosition.PositionType;


public class LtEncoder implements IndexConstraintTypeEncoder<Object>{
    private static final ConstraintTypePosition POS = new ConstraintTypePosition(PositionType.value,2);
    private static final String DEFAULT = "*";

    private IndexValueFactory indexValueFactory;
    public LtEncoder(IndexValueFactory indexValueFactory) {
        if(indexValueFactory == null){
            throw new IllegalArgumentException("The parsed IndexValueFactory MUST NOT be NULL!");
        }
        this.indexValueFactory = indexValueFactory;
    }
    @Override
    public void encode(EncodedConstraintParts constraint, Object value) {
        IndexValue indexValue;
        if(value == null){
            indexValue = null; //default value
        } else if(value instanceof IndexValue){
            indexValue = (IndexValue)value;
        } else {
            indexValue = indexValueFactory.createIndexValue(value);
        }
        String geConstraint = String.format("TO %s}",
                indexValue !=null && indexValue.getValue() != null && !indexValue.getValue().isEmpty() ?
                        indexValue.getValue() : DEFAULT);
        constraint.addEncoded(POS, geConstraint);
    }

    @Override
    public boolean supportsDefault() {
        return true;
    }

    @Override
    public Collection<IndexConstraintTypeEnum> dependsOn() {
        return Arrays.asList(IndexConstraintTypeEnum.GT);
    }

    @Override
    public IndexConstraintTypeEnum encodes() {
        return IndexConstraintTypeEnum.LT;
    }
    @Override
    public Class<Object> acceptsValueType() {
        return Object.class;
    }

}