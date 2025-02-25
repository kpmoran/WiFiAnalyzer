/*
 * WiFiAnalyzer
 * Copyright (C) 2019  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.vrem.wifianalyzer.wifi.timegraph;

import android.support.v4.widget.SwipeRefreshLayout;

import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.RobolectricUtil;
import com.vrem.wifianalyzer.wifi.scanner.ScannerService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.support.v4.SupportFragmentController;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class TimeGraphFragmentTest {

    private TimeGraphFragment fixture;
    private ScannerService scanner;

    @Before
    public void setUp() {
        RobolectricUtil.INSTANCE.getActivity();
        scanner = MainContextHelper.INSTANCE.getScannerService();
        fixture = SupportFragmentController.setupFragment(new TimeGraphFragment());
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testOnCreateView() {
        // validate
        assertNotNull(fixture);
        verify(scanner).update();
        verify(scanner).register(fixture.getTimeGraphAdapter());
    }

    @Test
    public void testOnResume() {
        // execute
        fixture.onResume();
        // validate
        verify(scanner, times(2)).update();
    }

    @Test
    public void testOnDestroy() {
        // execute
        fixture.onDestroy();
        // validate
        verify(scanner).unregister(fixture.getTimeGraphAdapter());
    }

    @Test
    public void testRefreshDisabled() {
        // validate
        SwipeRefreshLayout swipeRefreshLayout = fixture.getView().findViewById(R.id.graphRefresh);
        assertFalse(swipeRefreshLayout.isRefreshing());
        assertFalse(swipeRefreshLayout.isEnabled());
    }

}