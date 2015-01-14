/*
 * Copyright 2015 to CloudModelExplorer authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.xid.explorer.cdl;

import obp.literal.AnyLiteral;
import obp.literal.ArrayLiteral;
import obp.literal.BooleanLiteral;
import obp.literal.ConstantReference;
import obp.literal.IntegerLiteral;
import obp.literal.Literal;
import obp.literal.LiteralField;
import obp.literal.LiteralVisitor;
import obp.literal.RecordLiteral;
import obp.literal.StringLiteral;
import obp.literal.UnionLiteral;
import org.xid.explorer.ExplorationContext;
import org.xid.explorer.observation.Matcher;

import java.util.Stack;

/**
 * Created by j5r on 10/01/2015.
 */
public class LiteralToMatcher implements LiteralVisitor {

    /**
     * <p>Transforms given {@link Literal} to a {@link Matcher}.
     * @param literal to transform
     * @return a {@link Matcher}
     */
    public static Matcher toMatcher(Literal literal, ExplorationContext context) {
        LiteralToMatcher transformer = new LiteralToMatcher(context);
        literal.accept(transformer);
        return transformer.matcherStack.pop();
    }

    private final ExplorationContext context;
    private final Stack<Matcher> matcherStack = new Stack<Matcher>();

    private LiteralToMatcher(ExplorationContext context) {
        // protects constructor
        this.context = context;
    }

    @Override
    public void visitAnyLiteral(AnyLiteral toVisit) {
        matcherStack.push(Matcher.TRUE);
    }

    @Override
    public void visitStringLiteral(StringLiteral toVisit) {
        matcherStack.push(Matcher.constant(toVisit.getValue()));
    }

    @Override
    public void visitBooleanLiteral(BooleanLiteral toVisit) {
        matcherStack.push(Matcher.constant(toVisit.isValue()));
    }

    @Override
    public void visitIntegerLiteral(IntegerLiteral toVisit) {
        matcherStack.push(Matcher.constant(toVisit.getValue()));
    }

    @Override
    public void visitConstantReference(ConstantReference toVisit) {
        String name = toVisit.getName();
        String instanceName = null;
        String parameterName = null;
        int dotIndex = name.indexOf('.');
        if (dotIndex >= 0) {
            instanceName = name.substring(0, dotIndex);
            parameterName = name.substring(dotIndex+1, name.length());
        } else {
            parameterName = null;
        }
        Object value = context.getModelDescription().getParameterValue(instanceName, parameterName, Object.class, null);
        if (value == null) throw new IllegalArgumentException("Constant '"+ name +"' doesn't exist.");
        matcherStack.push(Matcher.constant(value));
    }

    @Override
    public void visitArrayLiteral(ArrayLiteral toVisit) {
        Matcher[] children = new Matcher[toVisit.getValueCount()];
        for ( int i=0; i<toVisit.getValueCount(); i++ ) {
            toVisit.getValue(i).accept(this);
            children[i] = Matcher.array(i, matcherStack.pop());
        }
        matcherStack.push(Matcher.and(children));
    }

    @Override
    public void visitLiteralField(LiteralField toVisit) {

    }

    @Override
    public void visitRecordLiteral(RecordLiteral toVisit) {

    }

    @Override
    public void visitUnionLiteral(UnionLiteral toVisit) {

    }
}
