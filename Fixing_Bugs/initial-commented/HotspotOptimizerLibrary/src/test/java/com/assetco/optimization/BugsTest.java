package com.assetco.optimization;

import com.assetco.hotspots.optimization.SearchResultHotspotOptimizer;
import com.assetco.search.results.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NotImplementedException extends RuntimeException {
}

/**
 * Created by John R. Donoghue on 3/20/21.
 */
public class BugsTest {
  private int idNumber = 0;
  private String[] names = {"missing1", "first", "second", "third", "fourth"};
  private final int maximumShowcaseItems = 5;
  private SearchResults searchResults;
  private AssetVendor partnerVendor;
  private SearchResultHotspotOptimizer optimizer;

  @BeforeEach
  void setUp() {
    optimizer = new SearchResultHotspotOptimizer();
    searchResults = new SearchResults();
    partnerVendor = AssetVendor.makeVendor();
  }

  @Test
  public void precedingPartnerWithLongTrailingAssetsDoesNotWin() {
    var otherVendor = AssetVendor.makeVendor();

    Asset missing = givenAssetInResultsWithVendor(partnerVendor);
    Asset disruptingAsset = givenAssetInResultsWithVendor(otherVendor);
    List<Asset> expected = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      expected.add(givenAssetInResultsWithVendor(partnerVendor));
    }
    whenOptimize();
    thenHotspotHas(HotspotKey.Showcase, List.of(missing));
    expected.add(missing);
    thenHotspotHasExactly(HotspotKey.Showcase, expected);
  }


  private Asset givenAssetInResultsWithVendor(AssetVendor vendor) {
    Asset result = getAsset(vendor);
    searchResults.addFound(result);
    return result;
  }

  private Asset getAsset(AssetVendor vendor) {
    return new Asset("anything", "anything", null, null,
                     getPurchaseInfo(), getPurchaseInfo(), new ArrayList<>(), vendor);
  }

  private AssetPurchaseInfo getPurchaseInfo() {
    return new AssetPurchaseInfo(0, 0,
        new Money(new BigDecimal("0")),
        new Money(new BigDecimal("0")));
  }

  private void whenOptimize() {
    optimizer.optimize(searchResults);
  }

  private void thenHotspotHas(HotspotKey key, List<Asset> forbidden) {
    var hotspot = searchResults.getHotspot(key);
    var members = hotspot.getMembers();
    for (var asset : forbidden) {
      assertTrue(members.contains(asset));
    }
  }

  private void thenHotspotHasExactly(HotspotKey key, List<Asset> expected) {
    /*
    Get the members of hotspot for the passed-in key and convert it to an array.
    Convert the expected list of assets to an array.
    Assert that the arrays are equal.
     */
    var actual = searchResults.getHotspot(key).getMembers();
    for (var item : expected) {
      assertTrue(actual.contains(item));
    }
    for (var item : actual){
      assertTrue(expected.contains(item));
    }
  }
}
