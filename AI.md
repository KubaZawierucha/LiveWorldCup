# Gemini

1. "I am writing a simple Java library with Maven build, consisting (probably) of a single service and its unit
   tests. Is there any sense of using Spring or Spring Boot for such project or will it be a kind of an
   over-engineering?"
2. "I have a Java program. I have a hashMap, where key is a string. Can I somehow check, if any key contains a certain
   substring? For example: I have key = "Canada_Mexico" and I wanted to return key (or key and value), having only "
   Mexico" string."\
   Answer:
   ```
   import java.util.Map;
   import java.util.stream.Collectors;
   // ... assuming map is Map<String, String>
   Map<String, String> matchedEntries = map.entrySet().stream()
   .filter(entry -> entry.getKey().contains("Mexico"))
   .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
   // You now have a map with only the entries where the key contains "Mexico"
   ```

# Copilot

1. "The score field will be updated frequently. Shouldn't it be a regular class instead of a Record?" +
   MatchParticipant.java in context
2. "I have added two tests testing startMatch() method from ScoreboardController. Can you generate other tests, only for
   this method?"
   ```
   @Test
   public void shouldThrowException_when_secondTeamNameIsNull() {
       // given:
       String home = "Mexico";
       String visitor = null;

       // then:
       Assertions.assertThrows(InvalidParticipantNameException.class, () -> scoreboardController.startMatch(home, visitor));
   }

   @Test
   public void shouldThrowException_when_firstTeamNameIsBlank() {
       // given:
       String home = " ";
       String visitor = "Canada";

       // then:
       Assertions.assertThrows(InvalidParticipantNameException.class, () -> scoreboardController.startMatch(home, visitor));
   }

   @Test
   public void shouldThrowException_when_secondTeamNameIsBlank() {
        // given:
        String home = "Mexico";
        String visitor = " ";

        // then:
        Assertions.assertThrows(InvalidParticipantNameException.class, () -> scoreboardController.startMatch(home, visitor));
   }

   @Test
   public void shouldThrowException_when_bothTeamNamesAreSame() {
        // given:
        String home = "Mexico";
        String visitor = "Mexico";

        // then:
        Assertions.assertThrows(InvalidParticipantNameException.class, () -> scoreboardController.startMatch(home, visitor));
   }
   ```
3. "Can you generate similar tests for increaseScore, decreaseScore and finishMatch methods?"
   ```
   @Test
   public void shouldThrowNoException_when_matchAndScorerAreValid() {
        // given:
        String home = "Mexico";
        String visitor = "Canada";
        scoreboardController.startMatch(home, visitor);

        // when:
        Assertions.assertDoesNotThrow(() -> scoreboardController.increaseScore(home, visitor, home));
    }

    @Test
    public void shouldThrowException_when_firstTeamNameIsNull_forIncreaseScore() {
        // given:
        String home = null;
        String visitor = "Canada";

        // then:
        Assertions.assertThrows(InvalidParticipantNameException.class, () -> scoreboardController.increaseScore(home, visitor, "Canada"));
    }

    @Test
    public void shouldThrowException_when_secondTeamNameIsNull_forIncreaseScore() {
        // given:
        String home = "Mexico";
        String visitor = null;

        // then:
        Assertions.assertThrows(InvalidParticipantNameException.class, () -> scoreboardController.increaseScore(home, visitor, "Mexico"));
    }

    @Test
    public void shouldThrowException_when_firstTeamNameIsBlank_forIncreaseScore() {
        // given:
        String home = " ";
        String visitor = "Canada";

        // then:
        Assertions.assertThrows(InvalidParticipantNameException.class, () -> scoreboardController.increaseScore(home, visitor, "Canada"));
    }

    @Test
    public void shouldThrowException_when_secondTeamNameIsBlank_forIncreaseScore() {
        // given:
        String home = "Mexico";
        String visitor = " ";

        // then:
        Assertions.assertThrows(InvalidParticipantNameException.class, () -> scoreboardController.increaseScore(home, visitor, "Mexico"));
    }

    @Test
    public void shouldThrowException_when_bothTeamNamesAreSame_forIncreaseScore() {
        // given:
        String home = "Mexico";
        String visitor = "Mexico";

        // then:
        Assertions.assertThrows(InvalidParticipantNameException.class, () -> scoreboardController.increaseScore(home, visitor, "Mexico"));
    }

    @Test
    public void shouldDecreaseHomeScore_when_matchAndScorerAreValid() {
        // given:
        String home = "Mexico";
        String visitor = "Canada";
        scoreboardController.startMatch(home, visitor);
        scoreboardController.increaseScore(home, visitor, home);

        // when:
        scoreboardController.decreaseScore(home, visitor, home);

        // then:
        assertTrue(scoreboard.getSummary().stream()
                .anyMatch(match -> match.home().getScore() == 0 && match.visitor().getScore() == 0)
        );
    }

    @Test
    public void shouldThrowException_when_firstTeamNameIsNull_forDecreaseScore() {
        // given:
        String home = null;
        String visitor = "Canada";

        // then:
        Assertions.assertThrows(InvalidParticipantNameException.class, () -> scoreboardController.decreaseScore(home, visitor, "Canada"));
    }

    @Test
    public void shouldThrowException_when_secondTeamNameIsNull_forDecreaseScore() {
        // given:
        String home = "Mexico";
        String visitor = null;

        // then:
        Assertions.assertThrows(InvalidParticipantNameException.class, () -> scoreboardController.decreaseScore(home, visitor, "Mexico"));
    }

    @Test
    public void shouldThrowException_when_firstTeamNameIsBlank_forDecreaseScore() {
        // given:
        String home = " ";
        String visitor = "Canada";

        // then:
        Assertions.assertThrows(InvalidParticipantNameException.class, () -> scoreboardController.decreaseScore(home, visitor, "Canada"));
    }

    @Test
    public void shouldThrowException_when_secondTeamNameIsBlank_forDecreaseScore() {
        // given:
        String home = "Mexico";
        String visitor = " ";

        // then:
        Assertions.assertThrows(InvalidParticipantNameException.class, () -> scoreboardController.decreaseScore(home, visitor, "Mexico"));
    }

    @Test
    public void shouldThrowException_when_bothTeamNamesAreSame_forDecreaseScore() {
        // given:
        String home = "Mexico";
        String visitor = "Mexico";

        // then:
        Assertions.assertThrows(InvalidParticipantNameException.class, () -> scoreboardController.decreaseScore(home, visitor, "Mexico"));
    }

    @Test
    public void shouldRemoveMatch_when_validMatchIsFinished() {
        // given:
        String home = "Mexico";
        String visitor = "Canada";
        scoreboardController.startMatch(home, visitor);

        // when:
        scoreboardController.finishMatch(home, visitor);

        // then:
        assertTrue(scoreboard.getSummary().stream()
                .noneMatch(match -> match.getHome().name().equals(home) && match.getVisitor().name().equals(visitor))
        );
    }

    @Test
    public void shouldThrowException_when_firstTeamNameIsNull_forFinishMatch() {
        // given:
        String home = null;
        String visitor = "Canada";

        // then:
        Assertions.assertThrows(InvalidParticipantNameException.class, () -> scoreboardController.finishMatch(home, visitor));
    }

    @Test
    public void shouldThrowException_when_secondTeamNameIsNull_forFinishMatch() {
        // given:
        String home = "Mexico";
        String visitor = null;

        // then:
        Assertions.assertThrows(InvalidParticipantNameException.class, () -> scoreboardController.finishMatch(home, visitor));
    }

    @Test
    public void shouldThrowException_when_firstTeamNameIsBlank_forFinishMatch() {
        // given:
        String home = " ";
        String visitor = "Canada";

        // then:
        Assertions.assertThrows(InvalidParticipantNameException.class, () -> scoreboardController.finishMatch(home, visitor));
    }

    @Test
    public void shouldThrowException_when_secondTeamNameIsBlank_forFinishMatch() {
        // given:
        String home = "Mexico";
        String visitor = " ";

        // then:
        Assertions.assertThrows(InvalidParticipantNameException.class, () -> scoreboardController.finishMatch(home, visitor));
    }

    @Test
    public void shouldThrowException_when_bothTeamNamesAreSame_forFinishMatch() {
        // given:
        String home = "Mexico";
        String visitor = "Mexico";

        // then:
        Assertions.assertThrows(InvalidParticipantNameException.class, () -> scoreboardController.finishMatch(home, visitor));
    }
4. "Can you refactor the tests to use Parametrized Tests?"
    - Current format of the test returned
5. "Take a look at the class. There are two methods testing startMatch method in LiveWorldCupScoreboard class. Can you
   generate other tests, just for this method?" (LiveWorldCupScoreboardTest in context)
6. "Can you now add a test cases for incrementScore, decrementScore and finishMatch, similar to what you did for the
   startMatch?"
7. "Scoreboard.getSummary() will return a specific list of Match objects. What assertion can I use?"
   ```
   List<String> expected = List.of(
       "Uruguay-Italy:6-6",
       "Spain-Brazil:10-2",
       "Mexico-Canada:0-5"
   );

   List<String> actual = scoreboard.getSummary().stream()
   .map(m -> m.home().getName().name() + "-" + m.visitor().getName().name()
   + ":" + m.home().getScore() + "-" + m.visitor().getScore()).toList();

   assertIterableEquals(expected, actual);
   ```
8. "Can you review this file? Assume this is a single threaded programm." (LiveWorldCupScoreboard in the context)
   ```
   increaseScore / decreaseScore are near-duplicates (lines 34–64) and re-implement participant lookup that already
   exists in Match.getParticipantByName. You could collapse both to:
   Match currentMatch = matches.get(matchId);
   if (currentMatch == null) {
       throw new NoActiveMatchException();
   }
   currentMatch.getParticipantByName(participantName).increaseScore();
   getParticipantByName already throws ParticipantNotFoundInMatchException, so behavior is preserved with less code.
   ```
9. "Take a look at shouldSortByTotalScoreThenByStartOrder_when_summaryRequested test. Can you find other way of
   comparing the result with the expected result?"
   ```
   to use the helper Record :
   private record MatchRow(String home, String visitor, int homeScore, int visitorScore, long order) {
       static MatchRow from(Match match) {
           return new MatchRow(
               match.home().getName().name(),
               match.visitor().getName().name(),
               match.home().getScore(),
               match.visitor().getScore(),
               match.order()
           );
       }
   }
   ```
10. "I would like to handle the multithreading in methods of this class. I've used ReentrantLock. This class is quite
    simple, wouldn't be the simple synchronized block better here?"
11. "What about the approaches with ConcurrentHashMap or AtomidLong for matchOrder/score in MatchParticipant."
12. "So taking everything what you just said into account, what type of synchronization would you use for this method?"
   ```
   For the whole class, I'd use synchronized on a private, dedicated lock object — a small refinement over the synchronized
   methods currently in place. Reasoning, pulling together everything above:
   • Coarse-grained, single lock is correct. Every method has compound invariants (check-then-act in
     startMatch/decreaseScore, multi-field consistency in getSummary/getTotalScore). One monitor guarding all of them is
     exactly right; finer-grained atomics would be redundant or unsafe.
   • synchronized over ReentrantLock — none of the Lock features are used, and you avoid lock/finally boilerplate.
   • Private lock object over synchronized methods — synchronized methods lock on this, which is publicly reachable, so any
     external code doing synchronized(scoreboard) could interfere or deadlock. A private lock encapsulates the monitor so the
     locking policy is fully owned by the class.
  ```
13. "Can you add a unit test for testing the newly added synchronization?"
   ```
   @Test
   public void shouldNotLoseScoreUpdates_when_manyThreadsIncreaseConcurrently() throws InterruptedException {
      // given:
      MatchParticipant home = new MatchParticipant(new ParticipantName("Mexico"));
      MatchParticipant visitor = new MatchParticipant(new ParticipantName("Canada"));
      scoreboard.startMatch(home, visitor);
      MatchId matchId = MatchId.fromMatchParticipants(home, visitor);

      int threads = 16;
      int incrementsPerThread = 1_000;
      ExecutorService executor = Executors.newFixedThreadPool(threads);
      CountDownLatch start = new CountDownLatch(1);
      CountDownLatch done = new CountDownLatch(threads);

      // when:
      for (int t = 0; t < threads; t++) {
          executor.submit(() -> {
              try {
                    start.await();
                    for (int i = 0; i < incrementsPerThread; i++) {
                        scoreboard.increaseScore(matchId, home.getName());
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            });
        }
        start.countDown();
        assertTrue(done.await(30, TimeUnit.SECONDS), "Concurrent increments did not finish in time");
        executor.shutdownNow();

        // then:
        int expectedScore = threads * incrementsPerThread;
        assertTrue(scoreboard.getSummary().stream()
                .anyMatch(match -> match.home().getScore() == expectedScore)
        );
    }

    @Test
    public void shouldStartExactlyOneMatch_when_manyThreadsStartOverlappingParticipantsConcurrently() throws InterruptedException {
        // given:
        int threads = 16;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);
        AtomicInteger successes = new AtomicInteger();
        AtomicInteger rejections = new AtomicInteger();

        // when: every thread tries to start a match sharing the "Mexico" participant
        for (int t = 0; t < threads; t++) {
            int index = t;
            executor.submit(() -> {
                try {
                    start.await();
                    MatchParticipant home = new MatchParticipant(new ParticipantName("Mexico"));
                    MatchParticipant visitor = new MatchParticipant(new ParticipantName("Opponent" + index));
                    scoreboard.startMatch(home, visitor);
                    successes.incrementAndGet();
                } catch (ParticipantAlreadyInMatchException e) {
                    rejections.incrementAndGet();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            });
        }
        start.countDown();
        assertTrue(done.await(30, TimeUnit.SECONDS), "Concurrent starts did not finish in time");
        executor.shutdownNow();

        // then: the check-then-act invariant held under contention
        assertEquals(1, successes.get());
        assertEquals(threads - 1, rejections.get());
        assertEquals(1, scoreboard.getSummary().size());
    }

    private record MatchInfo(String home, String visitor, int homeScore, int visitorScore, long order) {
        static MatchInfo from(Match match) {
            return new MatchInfo(
                    match.home().getName().name(),
                    match.visitor().getName().name(),
                    match.home().getScore(),
                    match.visitor().getScore(),
                    match.order()
            );
        }
    }
   ```
