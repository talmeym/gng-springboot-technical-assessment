package uk.emarte.jobhunting.gng.service;

record IpValidationResponse(
        String status,
        String message,
        String country,
        String countryCode,
        String region,
        String regionName,
        String city,
        String zip,
        String lat,
        String lon,
        String timezone,
        String isp,
        String org,
        String as,
        String query
) {
}
