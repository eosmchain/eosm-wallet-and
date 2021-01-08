package com.token.mangowallet.utils;

import com.blankj.utilcode.util.MapUtils;
import com.token.mangowallet.base.BaseFragment;
import com.token.mangowallet.test.TestFragment;
import com.token.mangowallet.ui.fragment.ActivateMidFragment;
import com.token.mangowallet.ui.fragment.AddGoodsFragment;
import com.token.mangowallet.ui.fragment.ExportPrivateKeyOrMnemonicsFragment;
import com.token.mangowallet.ui.fragment.NewRealTimeDateFragment;
import com.token.mangowallet.ui.fragment.RuleFragment;
import com.token.mangowallet.ui.fragment.StakeAddVoteFragment;
import com.token.mangowallet.ui.fragment.AppsInteriorFragment;
import com.token.mangowallet.ui.fragment.AssistCreateFragment;
import com.token.mangowallet.ui.fragment.BackupsMnemonicFragment;
import com.token.mangowallet.ui.fragment.BindETHFragment;
import com.token.mangowallet.ui.fragment.BuyOrSellRamFragment;
import com.token.mangowallet.ui.fragment.ChainStoreFragment;
import com.token.mangowallet.ui.fragment.ChangePasswordFragment;
import com.token.mangowallet.ui.fragment.ConfirmMnemonicFragment;
import com.token.mangowallet.ui.fragment.CreationWalletFragment;
import com.token.mangowallet.ui.fragment.CurrencySetupFragmeng;
import com.token.mangowallet.ui.fragment.EditAddressFragment;
import com.token.mangowallet.ui.fragment.EditStoreFragment;
import com.token.mangowallet.ui.fragment.ExtractionYieldFragment;
import com.token.mangowallet.ui.fragment.GoodsDetailsFragment;
import com.token.mangowallet.ui.fragment.GoodsManagerFragment;
import com.token.mangowallet.ui.fragment.ImportWalletFragment;
import com.token.mangowallet.ui.fragment.InvitationFragment;
import com.token.mangowallet.ui.fragment.LanguageFragment;
import com.token.mangowallet.ui.fragment.LifeFragment;
import com.token.mangowallet.ui.fragment.LifePaymentFragment;
import com.token.mangowallet.ui.fragment.LifePaymentRecordFragment;
import com.token.mangowallet.ui.fragment.MangoAssociationFragment;
import com.token.mangowallet.ui.fragment.MangoNodeFragment;
import com.token.mangowallet.ui.fragment.MarginFragment;
import com.token.mangowallet.ui.fragment.MarginRecordFragment;
import com.token.mangowallet.ui.fragment.MiningIndexFragment;
import com.token.mangowallet.ui.fragment.MiningMortgageFragment;
import com.token.mangowallet.ui.fragment.MixMortgageFragment;
import com.token.mangowallet.ui.fragment.MortgageBigFragment;
import com.token.mangowallet.ui.fragment.MortgageOrRedeemFragment;
import com.token.mangowallet.ui.fragment.MyStimulateFragment;
import com.token.mangowallet.ui.fragment.MyVoteMainFragment;
import com.token.mangowallet.ui.fragment.OperatingStepsFragment;
import com.token.mangowallet.ui.fragment.OrderCenterFragment;
import com.token.mangowallet.ui.fragment.OrderConfirmFragment;
import com.token.mangowallet.ui.fragment.OrderDetailsFragment;
import com.token.mangowallet.ui.fragment.ProClassifyfragment;
import com.token.mangowallet.ui.fragment.ProceedsAdressFragment;
import com.token.mangowallet.ui.fragment.RealTimeDateFragment;
import com.token.mangowallet.ui.fragment.ReceivingAddressFragment;
import com.token.mangowallet.ui.fragment.RecruitmentFragment;
import com.token.mangowallet.ui.fragment.ResourceAdministrationFragment;
import com.token.mangowallet.ui.fragment.SelectOrRegisterWalletFragment;
import com.token.mangowallet.ui.fragment.SendVoteMainFragment;
import com.token.mangowallet.ui.fragment.StakeVoteDetailsFragment;
import com.token.mangowallet.ui.fragment.StakeVoteListFragment;
import com.token.mangowallet.ui.fragment.StakeVoteMainFragment;
import com.token.mangowallet.ui.fragment.StakeVotePaymentFragment;
import com.token.mangowallet.ui.fragment.StimulateListFragment;
import com.token.mangowallet.ui.fragment.StoreFragment;
import com.token.mangowallet.ui.fragment.TransactionDetailsFragment;
import com.token.mangowallet.ui.fragment.TransactionRecordFragment;
import com.token.mangowallet.ui.fragment.TransferFragment;
import com.token.mangowallet.ui.fragment.VoteDetailsFragment;
import com.token.mangowallet.ui.fragment.VoteMainFragment;
import com.token.mangowallet.ui.fragment.VoteMainRecordFragment;
import com.token.mangowallet.ui.fragment.WalletListFragment;
import com.token.mangowallet.ui.fragment.WalletManagementFragment;
import com.token.mangowallet.ui.fragment.mgp_deal.BuyerTransactionInfoFragment;
import com.token.mangowallet.ui.fragment.mgp_deal.OTCDealFragment;
import com.token.mangowallet.ui.fragment.mgp_deal.OTCSellFragment;
import com.token.mangowallet.ui.fragment.mgp_deal.setup.OTCSetupFragment;
import com.token.mangowallet.ui.fragment.mgp_deal.setup.SetupContactFragment;
import com.token.mangowallet.ui.fragment.mgp_deal.setup.SetupPaymentFragment;
import com.token.mangowallet.ui.home.HomeFragment;

import java.util.Map;

public class MangoFragmentManager {
    private static MangoFragmentManager manager;
    private Map<String, Class<? extends BaseFragment>> mapFragments = MapUtils.newHashMap();

    public MangoFragmentManager() {
        initFragment();
    }

    public static MangoFragmentManager getInstance() {
        if (manager == null) {
            manager = new MangoFragmentManager();
        }
        return manager;
    }

    public void initFragment() {
        mapFragments.put("HomeFragment", HomeFragment.class);
//        mapFragments.put("WalletFragment", WalletFragment.class);
//        mapFragments.put("AppsFragment", AppsFragment.class);
//        mapFragments.put("MyFragment", MyFragment.class);
        mapFragments.put("TransactionRecordFragment", TransactionRecordFragment.class);
        mapFragments.put("ImportWalletFragment", ImportWalletFragment.class);
        mapFragments.put("CreationWalletFragment", CreationWalletFragment.class);
        mapFragments.put("ConfirmMnemonicFragment", ConfirmMnemonicFragment.class);
        mapFragments.put("BackupsMnemonicFragment", BackupsMnemonicFragment.class);
        mapFragments.put("SelectOrRegisterWalletFragment", SelectOrRegisterWalletFragment.class);
        mapFragments.put("ResourceAdministrationFragment", ResourceAdministrationFragment.class);
        mapFragments.put("MortgageOrRedeemFragment", MortgageOrRedeemFragment.class);
        mapFragments.put("WalletManagementFragment", WalletManagementFragment.class);
        mapFragments.put("ChangePasswordFragment", ChangePasswordFragment.class);
        mapFragments.put("ProceedsAdressFragment", ProceedsAdressFragment.class);
        mapFragments.put("TransactionDetailsFragment", TransactionDetailsFragment.class);
        mapFragments.put("BuyOrSellRamFragment", BuyOrSellRamFragment.class);
        mapFragments.put("TransferFragment", TransferFragment.class);
        mapFragments.put("MortgageBigFragment", MortgageBigFragment.class);
        mapFragments.put("MiningMortgageFragment", MiningMortgageFragment.class);
        mapFragments.put("StoreFragment", StoreFragment.class);
        mapFragments.put("AppsInteriorFragment", AppsInteriorFragment.class);
        mapFragments.put("ChainStoreFragment", ChainStoreFragment.class);
        mapFragments.put("GoodsDetailsFragment", GoodsDetailsFragment.class);
        mapFragments.put("OrderConfirmFragment", OrderConfirmFragment.class);
        mapFragments.put("EditAddressFragment", EditAddressFragment.class);
        mapFragments.put("ReceivingAddressFragment", ReceivingAddressFragment.class);
        mapFragments.put("WalletListFragment", WalletListFragment.class);
        mapFragments.put("OrderCenterFragment", OrderCenterFragment.class);
        mapFragments.put("AddGoodsFragment", AddGoodsFragment.class);
        mapFragments.put("GoodsManagerFragment", GoodsManagerFragment.class);
        mapFragments.put("AssistCreateFragment", AssistCreateFragment.class);
        mapFragments.put("ActivateMidFragment", ActivateMidFragment.class);
        mapFragments.put("MangoAssociationFragment", MangoAssociationFragment.class);
        mapFragments.put("MangoNodeFragment", MangoNodeFragment.class);
        mapFragments.put("RealTimeDateFragment", RealTimeDateFragment.class);
        mapFragments.put("ExtractionYieldFragment", ExtractionYieldFragment.class);
        mapFragments.put("MiningIndexFragment", MiningIndexFragment.class);
        mapFragments.put("StimulateListFragment", StimulateListFragment.class);
        mapFragments.put("MyStimulateFragment", MyStimulateFragment.class);
        mapFragments.put("LanguageFragment", LanguageFragment.class);
        mapFragments.put("OrderDetailsFragment", OrderDetailsFragment.class);
        mapFragments.put("CurrencySetupFragmeng", CurrencySetupFragmeng.class);
        mapFragments.put("LifeFragment", LifeFragment.class);
        mapFragments.put("LifePaymentFragment", LifePaymentFragment.class);
        mapFragments.put("EditStoreFragment", EditStoreFragment.class);
        mapFragments.put("RecruitmentFragment", RecruitmentFragment.class);
        mapFragments.put("ProClassifyfragment", ProClassifyfragment.class);
        mapFragments.put("TestFragment", TestFragment.class);
        mapFragments.put("MixMortgageFragment", MixMortgageFragment.class);
        mapFragments.put("OperatingStepsFragment", OperatingStepsFragment.class);
        mapFragments.put("MarginFragment", MarginFragment.class);
        mapFragments.put("MarginRecordFragment", MarginRecordFragment.class);
        mapFragments.put("BindETHFragment", BindETHFragment.class);
        mapFragments.put("VoteMainFragment", VoteMainFragment.class);
        mapFragments.put("SendVoteMainFragment", SendVoteMainFragment.class);
        mapFragments.put("VoteDetailsFragment", VoteDetailsFragment.class);
        mapFragments.put("MyVoteMainFragment", MyVoteMainFragment.class);
        mapFragments.put("VoteMainRecordFragment", VoteMainRecordFragment.class);
        mapFragments.put("LifePaymentRecordFragment", LifePaymentRecordFragment.class);
        mapFragments.put("InvitationFragment", InvitationFragment.class);
        mapFragments.put("StakeVoteMainFragment", StakeVoteMainFragment.class);
        mapFragments.put("StakeVoteDetailsFragment", StakeVoteDetailsFragment.class);
        mapFragments.put("StakeAddVoteFragment", StakeAddVoteFragment.class);
        mapFragments.put("StakeVotePaymentFragment", StakeVotePaymentFragment.class);
        mapFragments.put("StakeVoteListFragment", StakeVoteListFragment.class);
        mapFragments.put("RuleFragment", RuleFragment.class);
        mapFragments.put("NewRealTimeDateFragment", NewRealTimeDateFragment.class);
        mapFragments.put("ExportPrivateKeyOrMnemonicsFragment", ExportPrivateKeyOrMnemonicsFragment.class);
        mapFragments.put("OTCDealFragment", OTCDealFragment.class);
        mapFragments.put("BuyerTransactionInfoFragment", BuyerTransactionInfoFragment.class);
        mapFragments.put("OTCSellFragment", OTCSellFragment.class);
        mapFragments.put("OTCSetupFragment", OTCSetupFragment.class);
        mapFragments.put("SetupContactFragment", SetupContactFragment.class);
        mapFragments.put("SetupPaymentFragment", SetupPaymentFragment.class);
    }

    public Class<? extends BaseFragment> getFragment(String fragmentName) {
        return mapFragments.get(fragmentName);
    }
}
