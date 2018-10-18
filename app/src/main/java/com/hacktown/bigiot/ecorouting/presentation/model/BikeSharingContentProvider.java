package com.hacktown.bigiot.ecorouting.presentation.model;

import android.content.Context;
import android.widget.Toast;
import java.util.List;
import org.eclipse.bigiot.lib.android.Consumer;
import org.eclipse.bigiot.lib.android.IAuthenticationHandler;
import org.eclipse.bigiot.lib.android.IDiscoveryHandler;
import org.eclipse.bigiot.lib.android.IResponseHandler;
import org.eclipse.bigiot.lib.android.ISubscriptionHandler;
import org.eclipse.bigiot.lib.exceptions.IncompleteOfferingQueryException;
import org.eclipse.bigiot.lib.model.BigIotTypes;
import org.eclipse.bigiot.lib.model.BigIotTypes.LicenseType;
import org.eclipse.bigiot.lib.model.Price.Euros;
import org.eclipse.bigiot.lib.offering.AccessParameters;
import org.eclipse.bigiot.lib.offering.AccessResponse;
import org.eclipse.bigiot.lib.offering.OfferingCore;
import org.eclipse.bigiot.lib.offering.OfferingDescription;
import org.eclipse.bigiot.lib.query.OfferingQuery;

public class BikeSharingContentProvider implements IDiscoveryHandler, IResponseHandler,
    ISubscriptionHandler,
    IAuthenticationHandler {

    private static BikeSharingContentProvider instance;

    private static final String CONSUMER_ID = "Dama-BikeSharing";
    private static final String CONSUMER_SECRET = "ZQR9sO6GTMOf259lpbO9OQ==";
    private static final String MARKETPLACE_URI = "https://market.big-iot.org";

    private Consumer consumer = null;
    private OfferingCore offering = null;
    private OfferingDescription selectedOfferingDescription = null;

    private AccesResponseCallback callback;
    private Context context;

    public static BikeSharingContentProvider getInstance(Context context, AccesResponseCallback callback) {
        if(instance == null) {
            instance = new BikeSharingContentProvider(context, callback);
        }
        return instance;
    }

    public BikeSharingContentProvider(Context context, AccesResponseCallback callback) {
        this.callback = callback;
        this.context = context;
    }

    public boolean onAccessOffering() throws Exception {

        if (consumer == null) {

            consumer = new Consumer(CONSUMER_ID, MARKETPLACE_URI);
            consumer.authenticateByTask(CONSUMER_SECRET, this);

        } else {

            if (this.offering != null) {
                AccessParameters accessParameters = AccessParameters.create();
                //  .addRdfTypeValue(new RDFType("schema:longitude"), 41.4)
                //  .addRdfTypeValue(new RDFType("schema:latitude"), 2.17)
                //  .addRdfTypeValue(new RDFType("schema:geoRadius"), 1000);
                consumer.accessByTask(this.offering, accessParameters, this);
            }

        }

        return true;

    }

    @Override
    public void onAuthenticate(String result) {

        if (result.equals(IAuthenticationHandler.AUTHENTICATION_OK)) {

            Toast.makeText(context, "Authentication Successful!", Toast.LENGTH_SHORT).show();

            // CASE 1: Offering is already known - i.e. know discovery is needed and offering can be directly subscribed
            if (selectedOfferingDescription != null) {
                consumer.subscribeByTask(selectedOfferingDescription,
                    this);
                // END of CASE 1
            } else {
                // CASE 2: Offering not yet known, i.e. OfferingQuery is used to discovery matching offerings on Marketplace
                OfferingQuery query = null;
                try {
                    query = Consumer.createOfferingQuery("Bike Sharing Station")
                        .withName("Bike Sharing Station")
                        .withCategory("urn:big-iot:BikeSharingStationCategory")
                        .withPricingModel(BigIotTypes.PricingModel.PER_ACCESS)
                        .withMaxPrice(Euros.amount(0.1))
                        .withLicenseType(LicenseType.OPEN_DATA_LICENSE);
                } catch (IncompleteOfferingQueryException e) {
                    e.printStackTrace();
                }

                consumer.discoverByTask(query, this);
                // END OF CASE 2
            }
        }else {
                Toast
                    .makeText(context, "ERROR: Authentication on Marketplace failed - check secret",
                        Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDiscoveryResponse(OfferingQuery query, List<OfferingDescription> offeringDescriptions) {

        if ((offeringDescriptions != null) && !offeringDescriptions.isEmpty()) {

            OfferingDescription selectedOfferingDescription = offeringDescriptions.get(1);
            consumer.subscribeByTask(selectedOfferingDescription, this);
            Toast.makeText(context, "Offering found: " + selectedOfferingDescription.getId(), Toast.LENGTH_SHORT).show();

        }
        else {
            Toast.makeText(context, "No Offerings found", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSubscriptionResponse(OfferingDescription offeringDescription, OfferingCore offering) {

        if (offering != null) {

            this.offering = offering;
            Toast.makeText(context, "Subscription successful!", Toast.LENGTH_SHORT).show();

            AccessParameters accessParameters = AccessParameters.create();
            consumer.accessByTask(this.offering, accessParameters, this);

        }
        else {
            Toast.makeText(context, "Subscription failed!", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onAccessResponse(OfferingCore offeringCore, AccessResponse accessResponse) {
        if (accessResponse != null) {
            this.callback.onResponse(offeringCore, accessResponse);
        }
    }

    public interface AccesResponseCallback {
        void onResponse(OfferingCore offeringCore, AccessResponse accessResponse);
    }

}
