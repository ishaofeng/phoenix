package com.salesforce.phoenix.expression.function;

import java.util.Collections;
import java.util.List;

import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;

import com.salesforce.phoenix.compile.KeyPart;
import com.salesforce.phoenix.expression.Expression;
import com.salesforce.phoenix.query.KeyRange;
import com.salesforce.phoenix.schema.PColumn;
import com.salesforce.phoenix.schema.PDataType;
import com.salesforce.phoenix.util.ByteUtil;

abstract public class PrefixFunction extends ScalarFunction {
    public PrefixFunction() {
    }

    public PrefixFunction(List<Expression> children) {
        super(children);
    }

    @Override
    public int getKeyFormationTraversalIndex() {
        return preservesOrder() == OrderPreserving.NO ? NO_TRAVERSAL : 0;
    }
    
    protected boolean extractNode() {
        return false;
    }

    private static byte[] evaluateExpression(Expression rhs) {
        ImmutableBytesWritable ptr = new ImmutableBytesWritable();
        rhs.evaluate(null, ptr);
        byte[] key = ByteUtil.copyKeyBytesIfNecessary(ptr);
        return key;
    }
    
    @Override
    public KeyPart newKeyPart(final KeyPart childPart) {
        return new KeyPart() {

            @Override
            public PColumn getColumn() {
                return childPart.getColumn();
            }

            @Override
            public List<Expression> getExtractNodes() {
                return extractNode() ? Collections.<Expression>singletonList(PrefixFunction.this) : Collections.<Expression>emptyList();
            }

            @Override
            public KeyRange getKeyRange(CompareOp op, Expression rhs, int span) {
                byte[] key;
                KeyRange range;
                PDataType type = getColumn().getDataType();
                switch (op) {
                case EQUAL:
                    key = evaluateExpression(rhs);
                    range = type.getKeyRange(key, true, ByteUtil.nextKey(key), false);
                    break;
                case GREATER:
                    key = evaluateExpression(rhs);
                    range = type.getKeyRange(ByteUtil.nextKey(key), true, KeyRange.UNBOUND, false);
                    break;
                case LESS_OR_EQUAL:
                    key = evaluateExpression(rhs);
                    range = type.getKeyRange(KeyRange.UNBOUND, false, ByteUtil.nextKey(key), false);
                    break;
                default:
                    return childPart.getKeyRange(op, rhs, 1);
                }
                Integer length = getColumn().getByteSize();
                return length == null ? range : range.fill(length);
            }
        };
    }


}
