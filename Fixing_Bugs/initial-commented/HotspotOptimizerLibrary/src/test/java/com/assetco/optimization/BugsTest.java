package com.assetco.optimization;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.assetco.search.results.Asset;
import com.assetco.search.results.AssetVendor;
import com.assetco.search.results.HotspotKey;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John R. Donoghue on 3/20/21.
 */
public class BugsTest {


  @Test
  public void precedingPartnerWithLongTrailingAssetsDoesNotWin() {
    var partnerVendor = AssetVendor.makeVendor();
    var otherVendor = AssetVendor.makeVendor();
    Asset missing = givenAssetInResultsWithVendor(partnerVendor);
    Asset disruptingAsset = givenAssetInResultsWithVendor(otherVendor);
    List<Asset> expected = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      expected.add(givenAssetInResultsWithVendor(partnerVendor));
    }
    whenOptimize();
    thenHotspotDoesNotHave(HotspotKey.Showcase, missing);
    thenHotspotHasExactly(HotspotKey.Showcase, expected);
  }

  private void whenOptimize() {
  }

  private void thenHotspotDoesNotHave(HotspotKey key, Asset asset) {
  }

  private void thenHotspotHasExactly(HotspotKey key, List<Asset> expected) {
  }


  private Asset givenAssetInResultsWithVendor(AssetVendor vendor) {
    return null;
  }
}
