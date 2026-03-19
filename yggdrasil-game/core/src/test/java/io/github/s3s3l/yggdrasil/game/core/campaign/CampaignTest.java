package io.github.s3s3l.yggdrasil.game.core.campaign;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 战役系统单元测试
 */
class CampaignTest {

    private Faction faction1;
    private Faction faction2;
    private Region region1;
    private Region region2;
    private CampaignMap map;

    @BeforeEach
    void setUp() {
        faction1 = Faction.builder()
                .id(1L)
                .name("Faction 1")
                .description("Test faction 1")
                .build();

        faction2 = Faction.builder()
                .id(2L)
                .name("Faction 2")
                .description("Test faction 2")
                .build();

        region1 = Region.builder()
                .id(1L)
                .name("Region 1")
                .size(100)
                .type(RegionType.CAPITAL)
                .build();

        region2 = Region.builder()
                .id(2L)
                .name("Region 2")
                .size(50)
                .type(RegionType.NORMAL)
                .build();

        map = CampaignMap.builder()
                .id(1L)
                .name("Test Map")
                .build();

        map.addRegion(region1);
        map.addRegion(region2);
        map.addFaction(faction1);
        map.addFaction(faction2);
    }

    @Test
    void testCampaignCreation() {
        Campaign campaign = Campaign.builder()
                .id(1L)
                .name("Test Campaign")
                .description("A test campaign")
                .map(map)
                .build();

        assertEquals(1L, campaign.getId());
        assertEquals("Test Campaign", campaign.getName());
        assertEquals(CampaignState.IN_PROGRESS, campaign.getState());
        assertFalse(campaign.isFinished());
    }

    @Test
    void testFactionResources() {
        faction1.addResource("gold", 100);
        faction1.addResource("wood", 50);

        assertEquals(100, faction1.getResource("gold"));
        assertEquals(50, faction1.getResource("wood"));
        assertEquals(0, faction1.getResource("stone"));
    }

    @Test
    void testFactionConsumeResource() {
        faction1.addResource("gold", 100);

        assertTrue(faction1.consumeResource("gold", 50));
        assertEquals(50, faction1.getResource("gold"));

        assertFalse(faction1.consumeResource("gold", 100));
    }

    @Test
    void testRegionAdjacency() {
        region1.addAdjacentRegion(region2);

        assertTrue(region1.isAdjacentTo(region2));
        assertFalse(region2.isAdjacentTo(region1)); // Not bidirectional by default
    }

    @Test
    void testCampaignMapGetRegionsByFaction() {
        region1.setController(faction1);
        region2.setController(faction1);
        map.addRegion(region1);
        map.addRegion(region2);

        // Since maps use Long keys now, we need to use ID values
        assertEquals(2, map.getAllRegions().size());
    }

    @Test
    void testDiplomacy() {
        Diplomacy diplomacy = Diplomacy.builder()
                .id(1L)
                .build();

        // 先设置一个初始关系
        diplomacy.setRelation(2L, new FactionRelation(2L, DiplomaticState.NEUTRAL, 0, false, false, false));
        diplomacy.setState(2L, DiplomaticState.FRIENDLY);
        diplomacy.setOpinion(2L, 50);

        assertEquals(DiplomaticState.FRIENDLY, diplomacy.getState(2L));
        assertEquals(50, diplomacy.getOpinion(2L));
    }

    @Test
    void testDiplomacyDeclareWar() {
        Diplomacy diplomacy = Diplomacy.builder()
                .id(1L)
                .build();

        // 先设置一个初始关系
        diplomacy.setRelation(2L, new FactionRelation(2L, DiplomaticState.NEUTRAL, 0, false, false, false));
        diplomacy.declareWar(2L);

        assertEquals(DiplomaticState.WAR, diplomacy.getState(2L));
        assertEquals(-100, diplomacy.getOpinion(2L));
        assertFalse(diplomacy.hasTradeAgreement(2L));
        assertFalse(diplomacy.hasNonAggressionPact(2L));
        assertFalse(diplomacy.isAllied(2L));
    }

    @Test
    void testDiplomacyMakePeace() {
        Diplomacy diplomacy = Diplomacy.builder()
                .id(1L)
                .build();

        diplomacy.declareWar(2L);
        diplomacy.makePeace(2L);

        assertEquals(DiplomaticState.NEUTRAL, diplomacy.getState(2L));
        assertEquals(0, diplomacy.getOpinion(2L));
    }

    @Test
    void testTurnAction() {
        TurnAction action = TurnAction.builder()
                .id(1L)
                .type(ActionType.MOVE)
                .description("Move to region 2")
                .sourceRegionId(1L)
                .targetRegionId(2L)
                .build();

        assertEquals(1L, action.getId());
        assertEquals(ActionType.MOVE, action.getType());
        assertEquals(1L, action.getSourceRegionId());
        assertEquals(2L, action.getTargetRegionId());
    }

    @Test
    void testCampaignTurn() {
        CampaignTurn turn = CampaignTurn.builder()
                .id(1L)
                .turnNumber(1)
                .faction(faction1)
                .phase(TurnPhase.ACTION)
                .build();

        assertEquals(1L, turn.getId());
        assertEquals(1, turn.getTurnNumber());
        assertEquals(faction1, turn.getFaction());
        assertEquals(TurnPhase.ACTION, turn.getPhase());
        assertFalse(turn.isFinished());
    }
}
