package ch.wisv.payments.model.eventsync;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Copyright (c) 2016  W.I.S.V. 'Christiaan Huygens'
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ProductEventsSync extends EventsSync {

    /**
     * Price of the product.
     */
    private Double price;

    /**
     * Description of the product.
     */
    private String description;

    /**
     * Title of the product.
     */
    private String title;

    /**
     * UUID of the product.
     */
    private String key;

    /**
     * Enum string of the organizing Committee.
     */
    private String organizedBy;

}
