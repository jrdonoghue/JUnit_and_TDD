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
  private SearchResults searchResults;
  private int idNumber = 0;
  private String[] names = {"missing1", "first", "second", "third", "fourth"};

  @BeforeEach
  void setUp() {
    searchResults = new SearchResults();
  }

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
    thenHotspotDoesNotHave(HotspotKey.Showcase, List.of(missing));
    thenHotspotHasExactly(HotspotKey.Showcase, expected);
  }

  private Asset givenAssetInResultsWithVendor(AssetVendor vendor) {
    Asset asset = new Asset(
        idNumber, names[idNumber],
        null,
        null,
        new AssetPurchaseInfo(2, 2, new Money(new BigDecimal(1.00)),
            new Money(new BigDecimal(0.10)) ),
        new AssetPurchaseInfo(1, 1, new Money(new BigDecimal(0.50)),
            new Money(new BigDecimal(0.05)) ),
        new ArrayList<AssetTopic>(),
        vendor);
    searchResults.addFound(asset);
    return asset;
  }

  private void whenOptimize() {
    SearchResultHotspotOptimizer optimizer = new SearchResultHotspotOptimizer();
    optimizer.optimize(searchResults);
  }

  private void thenHotspotDoesNotHave(HotspotKey key, List<Asset> forbidden) {
    var hotspot = searchResults.getHotspot(key);
    var members = hotspot.getMembers();
    for (var asset : forbidden) {
      assertFalse(members.contains(asset));
    }
  }

  private void thenHotspotHasExactly(HotspotKey key, List<Asset> expectedList) {
    /*
    Get the members of hotspot for the passed-in key and convert it to an array.
    Convert the expected list of assets to an array.
    Assert that the arrays are equal.
     */
    Object[] actual = searchResults.getHotspot(key).getMembers().toArray();
    Object[] expected = expectedList.toArray();
    assertArrayEquals(expected, actual);;
  }
}
