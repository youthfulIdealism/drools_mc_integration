/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.minecraft.adapter.cmds;

import org.drools.game.core.api.BaseCommand;
import org.drools.game.core.api.Context;
import org.drools.game.model.api.Player;

public class SetPlayerParamCommand extends BaseCommand<Void> {

    private String param;
    private Integer value;

    public SetPlayerParamCommand( Player player, String param, Integer value ) {
        super( player );
        this.param = param;
        this.value = value;
    }

    @Override
    public Void execute( Context ctx ) {
        return null;
    }

    public String getParam() {
        return param;
    }

    public void setParam( String param ) {
        this.param = param;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue( Integer value ) {
        this.value = value;
    }

}
