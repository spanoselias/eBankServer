package service;

import dto.enums.Currency;
import utils.Pair;

import java.util.Map;

public final class ConversionRatesService {

    // This should be concurrent map since in a real-time application
    // will be updated continuously from a liquidity provider.
    private final static Map<Pair<Currency, Currency>, Double> sCurrencyToRateMap =
            Map.of(
                    Pair.create(Currency.EUR, Currency.USD), 1.09,
                    Pair.create(Currency.EUR, Currency.EUR), 1.0,
                    Pair.create(Currency.GBP, Currency.GBP), 1.0,
                    Pair.create(Currency.USD, Currency.USD), 1.0,
                    Pair.create(Currency.EUR, Currency.GBP), 0.8826,
                    Pair.create(Currency.USD, Currency.EUR), 0.9079,
                    Pair.create(Currency.USD, Currency.GBP), 0.8014,
                    Pair.create(Currency.GBP, Currency.EUR), 1.1328,
                    Pair.create(Currency.GBP, Currency.USD), 1.2477);

    public static double getConversationRate(final Currency fromCurrency,
                                             final Currency toCurrency) {

        return sCurrencyToRateMap.get(Pair.create(fromCurrency, toCurrency));
    }

    private ConversionRatesService() {}

}
